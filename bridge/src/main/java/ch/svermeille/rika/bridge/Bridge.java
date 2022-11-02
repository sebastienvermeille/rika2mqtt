package ch.svermeille.rika.bridge;

import ch.svermeille.rika.bridge.misc.EmailObfuscator;
import ch.svermeille.rika.firenet.RikaFirenetService;
import ch.svermeille.rika.firenet.exception.CouldNotAuthenticateToRikaFirenetException;
import ch.svermeille.rika.firenet.exception.InvalidStoveIdException;
import ch.svermeille.rika.firenet.exception.UnableToRetrieveRikaFirenetDataException;
import ch.svermeille.rika.firenet.model.StoveId;
import ch.svermeille.rika.mqtt.MqttService;
import com.google.gson.Gson;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Sebastien Vermeille
 */
@Component
@RequiredArgsConstructor
@Flogger
public class Bridge {

  private final RikaFirenetService rikaFirenetService;
  private final MqttService mqttService;

  private final Gson gson;

  @Value("${rika.email}")
  private String rikaEmailAccount;

  @Value("${bridge.reportInterval}")
  private Duration bridgeReportInterval;

  private final EmailObfuscator emailObfuscator;

  private final List<StoveId> stoveIds = new ArrayList<>();

  @PostConstruct
  void init() {
    log.atInfo().log("Initializing Rika2Mqtt bridge :");
    this.stoveIds.clear();
    this.stoveIds.addAll(this.rikaFirenetService.getStoves());

    final var maskedEmailAccount = this.emailObfuscator.maskEmailAddress(this.rikaEmailAccount);
    if(this.stoveIds.isEmpty()) {
      log.atSevere()
          .log("""
                Could not retrieve any stove linked with account %s. Please double-check your configuration.
              """, maskedEmailAccount);
    } else {
      log.atInfo().log("Found %s stoves linked with account %s.", this.stoveIds.size(), maskedEmailAccount);
    }

    log.atInfo().log("Will now retrieve status for declared stove(s) each %s and publish it back to mqtt.",
        this.bridgeReportInterval);

    publishToMqtt();
  }

  @Scheduled(fixedDelayString = "${bridge.reportInterval}")
  void publishToMqtt() {
    this.stoveIds.forEach(stoveId -> {
      try {
        final var status = this.rikaFirenetService.getStatus(stoveId);
        this.mqttService.publish(this.gson.toJson(status));
      } catch(final InvalidStoveIdException e) {
        this.stoveIds.remove(stoveId);
        log.atSevere()
            .withCause(e)
            .log(e.getMessage());
      } catch(final CouldNotAuthenticateToRikaFirenetException e) {
        log.atSevere()
            .withCause(e)
            .log(e.getMessage());
      } catch(final UnableToRetrieveRikaFirenetDataException e) {
        log.atSevere().log(e.getMessage(), e);
      }
    });
  }
}
