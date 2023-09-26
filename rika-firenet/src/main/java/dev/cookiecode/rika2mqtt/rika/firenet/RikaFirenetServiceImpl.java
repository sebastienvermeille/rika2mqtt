/*
 * The MIT License
 * Copyright © 2022 Sebastien Vermeille
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package dev.cookiecode.rika2mqtt.rika.firenet;

import com.google.common.annotations.VisibleForTesting;
import dev.cookiecode.rika2mqtt.rika.firenet.api.RikaFirenetApi;
import dev.cookiecode.rika2mqtt.rika.firenet.exception.CouldNotAuthenticateToRikaFirenetException;
import dev.cookiecode.rika2mqtt.rika.firenet.exception.InvalidStoveIdException;
import dev.cookiecode.rika2mqtt.rika.firenet.exception.OutdatedRevisionException;
import dev.cookiecode.rika2mqtt.rika.firenet.exception.UnableToControlRikaFirenetException;
import dev.cookiecode.rika2mqtt.rika.firenet.exception.UnableToRetrieveRikaFirenetDataException;
import dev.cookiecode.rika2mqtt.rika.firenet.mapper.UpdatableControlsMapper;
import dev.cookiecode.rika2mqtt.rika.firenet.model.Auth;
import dev.cookiecode.rika2mqtt.rika.firenet.model.StoveId;
import dev.cookiecode.rika2mqtt.rika.firenet.model.StoveStatus;
import dev.cookiecode.rika2mqtt.rika.firenet.model.UpdatableControls;
import dev.cookiecode.rika2mqtt.rika.firenet.model.UpdatableControls.Fields;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.flogger.Flogger;
import okhttp3.ResponseBody;
import org.apache.hc.client5.http.fluent.Form;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpStatus;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import retrofit2.Response;

/**
 * @author Sebastien Vermeille
 */
@Service
@RequiredArgsConstructor
@Flogger
public class RikaFirenetServiceImpl implements RikaFirenetService {

  static final String IGNORE_RECEIVED_PROPERTY_S_THIS_PROPERTY_IS_ALREADY_MANAGED =
      "Ignore received property '%s'. This property is already managed by rika2mqtt.";
  public static final String KEEP_ALIVE_NOTIFICATION =
      "[KeepAlive] Authenticated to rika-firenet as the bridge had no activity since more than %s";
  static final String AUTHENTICATED_SUCCESSFULLY = "Authenticated successfully to RIKA Firenet";
  private final RikaFirenetApi firenetApi;

  private final UpdatableControlsMapper updatableControlsMapper;

  @Value("${rika.email}")
  private String rikaFirenetUserEmail;

  @Value("${rika.password}")
  private String rikaFirenetUserPassword;

  @Value("${rika.keepAliveTimeout}")
  private Duration rikaFirenetKeepAliveTimeout;

  @Value("${rika.url:https://www.rika-firenet.com}")
  private String rikaFirenetApiBaseUrl;

  private boolean connected = false;

  private Instant lastConnectivity;

  @PostConstruct
  void init() {
    try {
      authenticate();
    } catch (final CouldNotAuthenticateToRikaFirenetException e) {
      log.atSevere().log(e.getMessage());
    }
  }

  @Scheduled(fixedRateString = "${rika.keepAliveTimeout}", initialDelay = 2000)
  void keepAlive() {
    if (this.lastConnectivity == null
        || Instant.now().isAfter(this.lastConnectivity.plus(this.rikaFirenetKeepAliveTimeout))) {
      try {
        authenticate();
        log.atFinest().log(KEEP_ALIVE_NOTIFICATION, this.rikaFirenetKeepAliveTimeout);
      } catch (final CouldNotAuthenticateToRikaFirenetException e) {
        log.atSevere().log(e.getMessage());
      }
    }
  }

  void authenticate() throws CouldNotAuthenticateToRikaFirenetException {
    try {
      final var query =
          this.firenetApi.authenticate(
              Auth.builder()
                  .email(this.rikaFirenetUserEmail)
                  .password(this.rikaFirenetUserPassword)
                  .build());

      final var response = query.execute();

      if (response.code() == 200 && isLogoutLinkRendered(response)) {
        // it does a redirect when successfully authenticated
        this.connected = true;
        this.lastConnectivity = Instant.now(Clock.systemUTC());
        log.atInfo().log(AUTHENTICATED_SUCCESSFULLY);
      } else {
        this.connected = false;
        throw new CouldNotAuthenticateToRikaFirenetException(
            "Could not authenticate to RIKA Firenet, please check your credentials.");
      }
    } catch (final IOException e) {
      this.connected = false;
      throw new CouldNotAuthenticateToRikaFirenetException(
          "Could not authenticate to RIKA Firenet, unable to establish a valid "
              + "communication with the rika firenet server.");
    }
  }

  void logout() {
    this.firenetApi.logout();
  }

  @SneakyThrows
  private boolean isLogoutLinkRendered(final Response<ResponseBody> response) {
    final var content = response.body().string();
    return content.contains("/web/logout");
  }

  @Override
  public boolean isAuthenticated() {
    return this.connected;
  }

  /**
   * @implNote This method use another HttpClient which is closable. This was required, as we need a
   *     standalone web context (session, cookies) not related at all with the retrofit ones
   *     (okhttp) Otherwise, after a successful login, even if you try with a wrong
   *     username:password -> it will succeed. Which is not good as this method is used to test
   *     changes in the configuration
   */
  @Override
  @SneakyThrows
  public boolean isValidCredentials(final String email, final String password) {
    try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
      var response =
          Request.post(rikaFirenetApiBaseUrl + "/web/login")
              .addHeader("Content-Type", "application/x-www-form-urlencoded")
              .bodyForm(Form.form().add("email", email).add("password", password).build())
              .execute(httpclient);
      var ct = response.returnContent().asString();
      return ct.contains("/web/logout");
    }
  }

  @Override
  public void updateControls(@NonNull StoveId stoveId, Map<String, String> fields)
      throws UnableToControlRikaFirenetException,
          InvalidStoveIdException,
          OutdatedRevisionException {
    try {
      var status = getStatus(stoveId);
      this.lastConnectivity = Instant.now(Clock.systemUTC());

      final var currentControls = updatableControlsMapper.toUpdateControls(status.getControls());

      overrideRevision(fields, currentControls);

      // patch current status with these new properties
      updatableControlsMapper.mergeWithMap(fields, currentControls);

      log.atFine().log("Send payload to rika-firenet: \n%s", currentControls);

      final var query = this.firenetApi.updateControls(stoveId.id().toString(), currentControls);
      final var response = query.execute();

      if (response.code() == HttpStatus.SC_OK) {
        log.atInfo().log(
            "Stove %s settings are now updated as follow: %s", stoveId, currentControls);
      } else {
        if (response.code() == HttpStatus.SC_NOT_FOUND) {
          var errorContent = response.errorBody().string();
          if (errorContent.startsWith(
              String.format("Stove %s is not registered for user", stoveId.id()))) {
            throw new InvalidStoveIdException(
                String.format(
                    "Could not control stove %s. %n cause reported by RIKA: %s",
                    stoveId, errorContent));
          } else if (errorContent.endsWith(" is outdated!")) {
            // revision is outdated -> retry once again
            throw new OutdatedRevisionException(
                String.format(
                    "Could not update settings of stove %s: Revision is outdated please retry.",
                    stoveId));
          } else {
            throw new UnableToControlRikaFirenetException(
                String.format(
                    "Could not update settings of stove %s: . %n cause reported by RIKA: %s",
                    stoveId, errorContent));
          }
        } else {
          throw new UnableToControlRikaFirenetException(
              String.format("Could not update settings of stove %s: Unknown reason.", stoveId));
        }
      }
    } catch (IOException
        | CouldNotAuthenticateToRikaFirenetException
        | UnableToRetrieveRikaFirenetDataException e) {
      throw new UnableToControlRikaFirenetException(
          String.format("Could not take control of stove %s. An error occurred.", stoveId), e);
    }
  }

  /**
   * Override revision property
   *
   * @implNote This property can't really be determined by the end user safely. Let's do that here.
   *     inspired from
   *     https://github.com/antibill51/rika-firenet-custom-component/blob/main/custom_components/rika_firenet/core.py
   */
  @VisibleForTesting
  void overrideRevision(Map<String, String> fields, UpdatableControls currentControls) {
    if (fields.containsKey(Fields.REVISION)) {
      log.atWarning().log(
          IGNORE_RECEIVED_PROPERTY_S_THIS_PROPERTY_IS_ALREADY_MANAGED, Fields.REVISION);
      fields.remove(Fields.REVISION); // this property is anyway override later
    }
    fields.put(UpdatableControls.Fields.REVISION, String.valueOf(currentControls.getRevision()));
  }

  @Override
  @SneakyThrows
  public List<StoveId> getStoves() {
    this.lastConnectivity = Instant.now(Clock.systemUTC());
    final var query = this.firenetApi.getStoves();
    final var response = query.execute();
    return extractStovesFromResponse(response);
  }

  @SneakyThrows
  private List<StoveId> extractStovesFromResponse(final Response<ResponseBody> response) {

    final var html = response.body().string();
    final var document = Jsoup.parse(html);
    final var pageLinks = document.select("a");

    final var results = new ArrayList<StoveId>();
    for (final var link : pageLinks) {
      final var href = link.attr("href");
      if (href.startsWith("/web/stove/")) {
        final var id = Long.parseLong(href.split("/")[3]);
        results.add(StoveId.of(id));
      }
    }

    return results;
  }

  @Override
  public StoveStatus getStatus(final @NonNull StoveId stoveId)
      throws InvalidStoveIdException,
          CouldNotAuthenticateToRikaFirenetException,
          UnableToRetrieveRikaFirenetDataException {
    try {
      final var stoveString = stoveId.id().toString();
      final var asyncCall = this.firenetApi.getStoveStatus(stoveString);
      final var response = asyncCall.execute();

      if (response.isSuccessful()) {
        this.lastConnectivity = Instant.now(Clock.systemUTC());
        return response.body();
      } else {
        if (response.code() == 500) {
          throw new InvalidStoveIdException(
              String.format(
                  "Could not retrieve status of stove %s.%n cause reported by RIKA: %s",
                  stoveId, response.errorBody().string()));
        } else if (response.code() == 401) {
          log.atWarning().log(
              "Tried to get status of stove %s, but the bridge was no longer authorized. Please check rika.keepAlive "
                  + "property.",
              stoveId);
          throw new CouldNotAuthenticateToRikaFirenetException(
              String.format(
                  "Could not retrieve stove %s status. %n cause: %s",
                  stoveId, response.errorBody().string()));
        }
        throw new InvalidStoveIdException(
            String.format(
                "Could not retrieve status of stove %s. %n cause reported by RIKA: %s",
                stoveId, response.errorBody().string()));
      }
    } catch (final IOException e) {
      throw new UnableToRetrieveRikaFirenetDataException(
          String.format("Could not retrieve stove %s status from rika-firenet", stoveId), e);
    }
  }
}
