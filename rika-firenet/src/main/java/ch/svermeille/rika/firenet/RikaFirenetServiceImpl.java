package ch.svermeille.rika.firenet;

import ch.svermeille.rika.firenet.api.RikaFirenetApi;
import ch.svermeille.rika.firenet.exception.CouldNotAuthenticateToRikaFirenetException;
import ch.svermeille.rika.firenet.exception.InvalidStoveIdException;
import ch.svermeille.rika.firenet.exception.UnableToRetrieveRikaFirenetDataException;
import ch.svermeille.rika.firenet.model.Auth;
import ch.svermeille.rika.firenet.model.StoveId;
import ch.svermeille.rika.firenet.model.StoveStatus;
import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.flogger.Flogger;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import retrofit2.Response;

/**
 * @author Sebastien Vermeille
 */
@Service
@RequiredArgsConstructor
@EnableScheduling // TODO: check if it should be there or on application only or  what is good practice
@Flogger
public class RikaFirenetServiceImpl implements RikaFirenetService {

  @Value("${rika.email}")
  private String rikaFirenetUserEmail;

  @Value("${rika.password}")
  private String rikaFirenetUserPassword;

  @Value("${rika.keepAliveTimeout}")
  private Duration rikaFirenetKeepAliveTimeout;

  private final RikaFirenetApi firenetApi;

  private boolean connected = false;

  private Instant lastConnectivity;

  @PostConstruct
  void init() {
    authenticate();
  }

  @Scheduled(fixedRateString = "${rika.keepAliveTimeout}", initialDelay = 5000)
    // TODO: do we need initialDelay or not ?
  void keepAlive() {
    if(lastConnectivity == null || Instant.now().isAfter(lastConnectivity.plus(rikaFirenetKeepAliveTimeout))) {
      authenticate();
      log.atFinest()
          .log("[KeepAlive] Authenticated to rika-firenet as the bridge had no activity since more than %s", rikaFirenetKeepAliveTimeout);
    }
  }

  @SneakyThrows // TODO: not sure this is a clean way of handling exceptions... should be reviewed
  void authenticate() {
    final var query = firenetApi.authenticate(
        Auth.builder()
            .email(rikaFirenetUserEmail)
            .password(rikaFirenetUserPassword)
            .build()
    );

    final var response = query.execute();

    if(response.code() == 200 && isLogoutLinkRendered(response)) {
      // it does a redirect when successfully authenticated
      connected = true;
      lastConnectivity = Instant.now(Clock.systemUTC());
      log.atInfo().log("Authenticated successfully to RIKA Firenet");
    } else {
      connected = false;
      log.atSevere().log("Could not authenticate to RIKA Firenet, please check your credentials.");
    }
  }

  void logout() {
    firenetApi.logout();
  }

  @SneakyThrows
  private boolean isLogoutLinkRendered(final Response<ResponseBody> response) {
    final var content = response.body().string();
    return content.contains("/web/logout");
  }

  boolean isAuthenticated() {
    return connected;
  }

  @Override
  @SneakyThrows
  public List<StoveId> getStoves() {
    lastConnectivity = Instant.now(Clock.systemUTC());
    final var query = firenetApi.getStoves();
    final var response = query.execute();
    return extractStovesFromResponse(response);
  }

  @SneakyThrows
  private List<StoveId> extractStovesFromResponse(final Response<ResponseBody> response) {

    final var html = response.body().string();
    final var document = Jsoup.parse(html);
    final var pageLinks = document.select("a");

    final var results = new ArrayList<StoveId>();
    for(var link : pageLinks) {
      final var href = link.attr("href");
      if(href.startsWith("/web/stove/")) {
        final var id = Long.parseLong(href.split("/")[3]);
        results.add(new StoveId(id));
      }
    }

    return results;
  }

  @Override
  public StoveStatus getStatus(final @NonNull StoveId stoveId) throws InvalidStoveIdException, CouldNotAuthenticateToRikaFirenetException, UnableToRetrieveRikaFirenetDataException {
    try {
      final var stoveString = stoveId.id().toString();
      final var asyncCall = firenetApi.getStoveStatus(stoveString);
      final var response = asyncCall.execute();

      if(response.isSuccessful()) {
        lastConnectivity = Instant.now(Clock.systemUTC());
        return response.body();
      } else {
        if(response.code() == 500) {
          throw new InvalidStoveIdException(
              String.format("Could not retrieve status of stove %s.%n cause reported by RIKA: %s",
                  stoveId,
                  response.errorBody().string()
              )
          );
        } else if(response.code() == 401) {
          log.atWarning().log("Tried to get status of stove %s, but the bridge was no longer authorized. Please check rika.keepAlive " +
              "property.", stoveId);
          throw new CouldNotAuthenticateToRikaFirenetException(String.format("Could not retrieve stove %s status. %n cause: %s", stoveId,
              response.errorBody().string()));
        }
        throw new InvalidStoveIdException(
            String.format("Could not retrieve status of stove %s. %n cause reported by RIKA: %s",
                stoveId,
                response.errorBody().string()
            )
        );
      }
    } catch(IOException e) {
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
