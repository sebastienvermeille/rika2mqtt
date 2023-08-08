/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package dev.cookiecode.rika2mqtt.bridge;

import com.google.gson.Gson;
import dev.cookiecode.rika2mqtt.bridge.misc.EmailObfuscator;
import dev.cookiecode.rika2mqtt.rika.firenet.RikaFirenetService;
import dev.cookiecode.rika2mqtt.rika.firenet.exception.CouldNotAuthenticateToRikaFirenetException;
import dev.cookiecode.rika2mqtt.rika.firenet.exception.InvalidStoveIdException;
import dev.cookiecode.rika2mqtt.rika.firenet.exception.UnableToRetrieveRikaFirenetDataException;
import dev.cookiecode.rika2mqtt.rika.firenet.model.StoveId;
import dev.cookiecode.rika2mqtt.rika.firenet.model.StoveStatus;
import dev.cookiecode.rika2mqtt.rika.firenet.model.UpdatableControls;
import dev.cookiecode.rika2mqtt.rika.firenet.model.UpdatableControls.Fields;
import dev.cookiecode.rika2mqtt.rika.mqtt.MqttService;
import dev.cookiecode.rika2mqtt.rika.mqtt.event.MqttCommandEvent;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
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

  @Value("${rika.email}")
  private String rikaEmailAccount;

  @Value("${bridge.reportInterval}")
  private Duration bridgeReportInterval;

  private final EmailObfuscator emailObfuscator;

  private final List<StoveId> stoveIds = new ArrayList<>();

  @PostConstruct
  void init() {
    log.atInfo().log("Initializing Rika2Mqtt bridge :");
    stoveIds.clear();
    stoveIds.addAll(rikaFirenetService.getStoves());

    var maskedEmailAccount = emailObfuscator.maskEmailAddress(rikaEmailAccount);
    if (stoveIds.isEmpty()) {
      log.atSevere().log(
          "Could not retrieve any stove linked with account %s. Please double-check your configuration.",
          maskedEmailAccount);
    } else {
      log.atInfo()
          .log("Found %s stoves linked with account %s.", stoveIds.size(), maskedEmailAccount);
      log.atInfo().log(
          "Will now retrieve status for declared stove(s) each %s and publish it back to mqtt.",
          bridgeReportInterval);

      publishToMqtt();
    }
  }

  /**
   * Poll rika-firenet stove status regularly and publish to MQTT
   */
  @Scheduled(fixedDelayString = "${bridge.reportInterval}")
  void publishToMqtt() {
    stoveIds.forEach(stoveId -> {
      final StoveStatus status;

      try {
        status = rikaFirenetService.getStatus(stoveId);
        Gson gson = new Gson();
        mqttService.publish(gson.toJson(status));
      } catch (InvalidStoveIdException e) {
        this.stoveIds.remove(stoveId);
        log.atSevere().log(e.getMessage(), e);
      } catch (CouldNotAuthenticateToRikaFirenetException e) {
        log.atSevere()
            .withCause(e)
            .log(e.getMessage());
      } catch (UnableToRetrieveRikaFirenetDataException e) {
        log.atSevere().log(e.getMessage(), e);
      }
    });
  }

  /**
   * Forward MQTT commands to rika-firenet
   */
  @EventListener(MqttCommandEvent.class)
  public void onReceiveMqttCommand(@NonNull MqttCommandEvent event) {
    try {
      log.atInfo()
          .log("Received mqtt command for stove: %s", event.getStoveId().toString());

      rikaFirenetService.updateControls(new StoveId(event.getStoveId()), event.getProps());
    } catch (Exception ex) {
      log.atSevere().log(ex.getMessage());
    }
  }
}
