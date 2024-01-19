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
package dev.cookiecode.rika2mqtt.bridge;

import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
import dev.cookiecode.rika2mqtt.bridge.misc.EmailObfuscator;
import dev.cookiecode.rika2mqtt.plugins.internal.v1.Rika2MqttPluginService;
import dev.cookiecode.rika2mqtt.plugins.internal.v1.event.PolledStoveStatusEvent;
import dev.cookiecode.rika2mqtt.plugins.internal.v1.event.StoveErrorEvent;
import dev.cookiecode.rika2mqtt.plugins.internal.v1.mapper.StoveErrorMapper;
import dev.cookiecode.rika2mqtt.plugins.internal.v1.mapper.StoveStatusMapper;
import dev.cookiecode.rika2mqtt.rika.firenet.RikaFirenetService;
import dev.cookiecode.rika2mqtt.rika.firenet.exception.CouldNotAuthenticateToRikaFirenetException;
import dev.cookiecode.rika2mqtt.rika.firenet.exception.InvalidStoveIdException;
import dev.cookiecode.rika2mqtt.rika.firenet.exception.OutdatedRevisionException;
import dev.cookiecode.rika2mqtt.rika.firenet.exception.UnableToControlRikaFirenetException;
import dev.cookiecode.rika2mqtt.rika.firenet.exception.UnableToRetrieveRikaFirenetDataException;
import dev.cookiecode.rika2mqtt.rika.firenet.model.StoveId;
import dev.cookiecode.rika2mqtt.rika.firenet.model.StoveStatus;
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
import org.springframework.context.ApplicationEventPublisher;
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

  static final String INITIALIZING_BRIDGE = "Initializing Rika2Mqtt bridge :";
  static final String RECEIVED_MQTT_COMMAND_FOR_STOVE_S = "Received mqtt command for stove: %s";
  static final String COULD_NOT_PROCESS_THE_RECEIVED_MQTT_COMMAND_S =
      "Could not process the received mqtt command: %s";
  static final String
      COULD_NOT_RETRIEVE_ANY_STOVE_LINKED_WITH_ACCOUNT_S_PLEASE_DOUBLE_CHECK_YOUR_CONFIGURATION =
          "Could not retrieve any stove linked with account %s. Please double-check your configuration.";
  static final String
      WILL_NOW_RETRIEVE_STATUS_FOR_EACH_DECLARED_STOVES_AT_INTERVAL_OF_S_AND_PUBLISH_IT_BACK_TO_MQTT =
          "Will now retrieve status for each declared stove(s) at interval of %s and publish it back to mqtt.";
  private final RikaFirenetService rikaFirenetService;
  private final MqttService mqttService;

  @Value("${rika.email}")
  private String rikaEmailAccount;

  @Value("${bridge.reportInterval}")
  private Duration bridgeReportInterval;

  private final EmailObfuscator emailObfuscator;
  private final Gson gson;
  private final StoveStatusMapper stoveStatusMapper;
  private final StoveErrorMapper stoveErrorMapper;

  private final Rika2MqttPluginService pluginManager;

  private final ApplicationEventPublisher applicationEventPublisher;

  private final List<StoveId> stoveIds = new ArrayList<>();

  @PostConstruct
  void init() {
    log.atInfo().log(INITIALIZING_BRIDGE);

    initStoves(rikaFirenetService.getStoves());
    printStartupMessages();
    pluginManager.start();
    publishToMqtt();
  }

  /**
   * @implNote should not log messages ideally. reason: {@link Bridge#printStartupMessages()} will
   *     print the supposed first message
   */
  @VisibleForTesting
  void initStoves(List<StoveId> stoveIds) {
    this.stoveIds.clear();
    this.stoveIds.addAll(stoveIds);
  }

  @VisibleForTesting
  void printStartupMessages() {
    var maskedEmailAccount = emailObfuscator.maskEmailAddress(rikaEmailAccount);
    if (stoveIds.isEmpty()) {
      log.atSevere().log(
          COULD_NOT_RETRIEVE_ANY_STOVE_LINKED_WITH_ACCOUNT_S_PLEASE_DOUBLE_CHECK_YOUR_CONFIGURATION,
          maskedEmailAccount);
    } else {
      log.atInfo().log(
          "Found %s stoves linked with account %s.", stoveIds.size(), maskedEmailAccount);
      log.atInfo().log(
          WILL_NOW_RETRIEVE_STATUS_FOR_EACH_DECLARED_STOVES_AT_INTERVAL_OF_S_AND_PUBLISH_IT_BACK_TO_MQTT,
          bridgeReportInterval);
    }
  }

  /** Poll rika-firenet stove status regularly and publish to MQTT */
  @Scheduled(fixedDelayString = "${bridge.reportInterval}")
  void publishToMqtt() {
    stoveIds.forEach(
        stoveId -> {
          final StoveStatus status;

          try {
            status = rikaFirenetService.getStatus(stoveId);
            final var jsonStatus = gson.toJson(status);
            mqttService.publish(jsonStatus);

            applicationEventPublisher.publishEvent(
                PolledStoveStatusEvent.builder()
                    .stoveStatus(stoveStatusMapper.toApiStoveStatus(status))
                    .build());

            status
                .getError()
                .ifPresent(
                    stoveError -> {
                      final var enrichedStoveError =
                          stoveErrorMapper.toApiStoveError(stoveId.id(), stoveError);
                      final var jsonError = gson.toJson(enrichedStoveError);
                      mqttService.publishError(jsonError);

                      applicationEventPublisher.publishEvent(
                          StoveErrorEvent.builder().stoveError(enrichedStoveError).build());
                    });
          } catch (InvalidStoveIdException e) {
            // TODO: could occurs if a stove is added later (after deployment of this rika2mqtt
            // instance, might be valuable to perform a reload of stoves "periodically") -> should
            // be evaluated
            this.stoveIds.remove(stoveId);
            log.atSevere().log(e.getMessage(), e);
          } catch (CouldNotAuthenticateToRikaFirenetException e) {
            log.atSevere().withCause(e).log(
                e.getMessage()); // TODO: implement a strategy (goal: avoid ban if each minutes
            // we try to auth with wrong credentials...)
            // That could happens if for some reason as user you change your password but forget
            // about your rika2mqtt instance.
          } catch (UnableToRetrieveRikaFirenetDataException e) {
            log.atSevere().log(e.getMessage(), e);
          }
        });
  }

  /** Forward MQTT commands to rika-firenet */
  @EventListener(MqttCommandEvent.class)
  public void onReceiveMqttCommand(@NonNull MqttCommandEvent event) {
    try {
      log.atInfo().log(RECEIVED_MQTT_COMMAND_FOR_STOVE_S, event.getStoveId().toString());

      rikaFirenetService.updateControls(StoveId.of(event.getStoveId()), event.getProps());
    } catch (UnableToControlRikaFirenetException | InvalidStoveIdException ex) {
      log.atSevere().withCause(ex).log(
          COULD_NOT_PROCESS_THE_RECEIVED_MQTT_COMMAND_S, ex.getMessage());
    } catch (OutdatedRevisionException ex) {
      log.atWarning().withCause(ex).log(
          COULD_NOT_PROCESS_THE_RECEIVED_MQTT_COMMAND_S, ex.getMessage());
      // TODO: implement a retry policy (once at least)
    }
  }
}
