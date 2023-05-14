/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.svermeille.rika.firenet;

import ch.svermeille.rika.firenet.api.RikaFirenetApi;
import ch.svermeille.rika.firenet.exception.CouldNotAuthenticateToRikaFirenetException;
import ch.svermeille.rika.firenet.exception.InvalidStoveIdException;
import ch.svermeille.rika.firenet.exception.UnableToRetrieveRikaFirenetDataException;
import ch.svermeille.rika.firenet.model.Auth;
import ch.svermeille.rika.firenet.model.StoveId;
import ch.svermeille.rika.firenet.model.StoveStatus;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.flogger.Flogger;
import okhttp3.ResponseBody;
import org.apache.hc.client5.http.fluent.Form;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
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

  private final RikaFirenetApi firenetApi;

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
    if (this.lastConnectivity == null || Instant.now()
        .isAfter(this.lastConnectivity.plus(this.rikaFirenetKeepAliveTimeout))) {
      try {
        authenticate();
        log.atFinest()
            .log(
                "[KeepAlive] Authenticated to rika-firenet as the bridge had no activity since more than %s",
                this.rikaFirenetKeepAliveTimeout);
      } catch (final CouldNotAuthenticateToRikaFirenetException e) {
        log.atSevere().log(e.getMessage());
      }
    }
  }

  void authenticate() throws CouldNotAuthenticateToRikaFirenetException {
    try {
      final var query = this.firenetApi.authenticate(
          Auth.builder()
              .email(this.rikaFirenetUserEmail)
              .password(this.rikaFirenetUserPassword)
              .build()
      );

      final var response = query.execute();

      if (response.code() == 200 && isLogoutLinkRendered(response)) {
        // it does a redirect when successfully authenticated
        this.connected = true;
        this.lastConnectivity = Instant.now(Clock.systemUTC());
        log.atInfo().log("Authenticated successfully to RIKA Firenet");
      } else {
        this.connected = false;
        throw new CouldNotAuthenticateToRikaFirenetException(
            "Could not authenticate to RIKA Firenet, please check your credentials.");
      }
    } catch (final IOException e) {
      this.connected = false;
      throw new CouldNotAuthenticateToRikaFirenetException(
          "Could not authenticate to RIKA Firenet, unable to establish a valid " +
              "communication with the rika firenet server.");
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
   * standalone web context (session, cookies) not related at all with the retrofit ones (okhttp)
   * Otherwise, after a successfull login, even if you try with a wrong username:password -> it will
   * succeed. Which is not good as this method is used to test changes in the configuration
   */
  @Override
  @SneakyThrows
  public boolean isValidCredentials(final String email, final String password) {
    try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
      var response = Request.post(rikaFirenetApiBaseUrl + "/web/login")
          .addHeader("Content-Type", "application/x-www-form-urlencoded")
          .bodyForm(Form.form().add("email", email).add("password", password).build())
          .execute(httpclient);
      var ct = response.returnContent().asString();
      return ct.contains("/web/logout");
    }
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
        results.add(new StoveId(id));
      }
    }

    return results;
  }

  @Override
  public StoveStatus getStatus(
      final @NonNull StoveId stoveId)
      throws InvalidStoveIdException, CouldNotAuthenticateToRikaFirenetException, UnableToRetrieveRikaFirenetDataException {
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
              String.format("Could not retrieve status of stove %s.%n cause reported by RIKA: %s",
                  stoveId,
                  response.errorBody().string()
              )
          );
        } else if (response.code() == 401) {
          log.atWarning().log(
              "Tried to get status of stove %s, but the bridge was no longer authorized. Please check rika.keepAlive "
                  +
                  "property.", stoveId);
          throw new CouldNotAuthenticateToRikaFirenetException(
              String.format("Could not retrieve stove %s status. %n cause: %s", stoveId,
                  response.errorBody().string()));
        }
        throw new InvalidStoveIdException(
            String.format("Could not retrieve status of stove %s. %n cause reported by RIKA: %s",
                stoveId,
                response.errorBody().string()
            )
        );
      }
    } catch (final IOException e) {
      throw new UnableToRetrieveRikaFirenetDataException(
          String.format(
              "Could not retrieve stove %s status from rika firenet",
              stoveId
          ),
          e
      );
    }
  }

}
