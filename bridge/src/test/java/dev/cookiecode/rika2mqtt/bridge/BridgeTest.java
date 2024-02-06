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

import static dev.cookiecode.rika2mqtt.bridge.Bridge.*;
import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import dev.cookiecode.rika2mqtt.bridge.misc.EmailObfuscator;
import dev.cookiecode.rika2mqtt.plugins.internal.v1.Rika2MqttPluginService;
import dev.cookiecode.rika2mqtt.plugins.internal.v1.mapper.StoveErrorMapper;
import dev.cookiecode.rika2mqtt.plugins.internal.v1.mapper.StoveStatusMapper;
import dev.cookiecode.rika2mqtt.rika.firenet.RikaFirenetService;
import dev.cookiecode.rika2mqtt.rika.firenet.model.StoveError;
import dev.cookiecode.rika2mqtt.rika.firenet.model.StoveId;
import dev.cookiecode.rika2mqtt.rika.firenet.model.StoveStatus;
import dev.cookiecode.rika2mqtt.rika.mqtt.MqttPublicationService;
import dev.cookiecode.rika2mqtt.rika.mqtt.event.MqttCommandEvent;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Test class
 *
 * @author Sebastien Vermeille
 */
@ExtendWith(MockitoExtension.class)
@ExtendWith(OutputCaptureExtension.class)
class BridgeTest {

  @Mock RikaFirenetService rikaFirenetService;
  @Mock MqttPublicationService mqttPublicationService;
  @Mock EmailObfuscator emailObfuscator;
  @Mock Gson gson;
  @Mock StoveStatusMapper stoveStatusMapper;
  @Mock StoveErrorMapper stoveErrorMapper;

  @Mock Rika2MqttPluginService pluginManager;

  @Mock ApplicationEventPublisher applicationEventPublisher;
  @InjectMocks @Spy Bridge bridge;

  @BeforeEach
  void setUp() {
    bridge.initStoves(emptyList()); // reset list
  }

  @Test
  void initShouldInitStovesWithRetrieveStovesFromRikaFirenet() throws Exception {
    // GIVEN
    List<StoveId> stoveIds = List.of(StoveId.of(15L));
    when(rikaFirenetService.getStoves()).thenReturn(stoveIds);
    when(rikaFirenetService.getStatus(any())).thenReturn(mock(StoveStatus.class));

    // WHEN
    bridge.init();

    // THEN
    verify(rikaFirenetService, times(1)).getStoves();
    verify(bridge, times(1)).initStoves(stoveIds);
  }

  @Test
  void initShouldInvokePrintStartupMessages() throws Exception {
    // GIVEN
    when(rikaFirenetService.getStoves()).thenReturn(List.of(StoveId.of(15L)));
    when(rikaFirenetService.getStatus(any())).thenReturn(mock(StoveStatus.class));

    // WHEN
    bridge.init();

    // THEN
    verify(bridge, times(1)).printStartupMessages();
  }

  @Test
  void printStartupMessagesShouldObfuscateEmail() {
    // GIVEN
    // nothing special

    // WHEN
    bridge.printStartupMessages();

    // THEN
    verify(emailObfuscator, times(1)).maskEmailAddress(any());
  }

  @Test
  void printStartupMessagesShouldPrintLogMessageWhenNoStovesAreLinkedToTheAccount(
      CapturedOutput output) {
    // GIVEN
    bridge.initStoves(emptyList());

    // WHEN
    bridge.printStartupMessages();

    // THEN
    await()
        .atMost(5, SECONDS)
        .untilAsserted(
            () ->
                assertTrue(
                    output
                        .getAll()
                        .contains(
                            format(
                                COULD_NOT_RETRIEVE_ANY_STOVE_LINKED_WITH_ACCOUNT_S_PLEASE_DOUBLE_CHECK_YOUR_CONFIGURATION,
                                (String) null))));
  }

  @Test
  void printStartupMessagesShouldPrintLogMessageWhenStovesAreLinkedToTheAccount(
      CapturedOutput output) {
    // GIVEN
    List<StoveId> stoveIds = List.of(StoveId.of(15L));
    bridge.initStoves(stoveIds);
    when(emailObfuscator.maskEmailAddress(any())).thenReturn("someEmail");

    // WHEN
    bridge.printStartupMessages();

    // THEN
    await()
        .atMost(5, SECONDS)
        .untilAsserted(
            () ->
                assertTrue(
                    output
                        .getAll()
                        .contains(
                            format(
                                WILL_NOW_RETRIEVE_STATUS_FOR_EACH_DECLARED_STOVES_AT_INTERVAL_OF_S_AND_PUBLISH_IT_BACK_TO_MQTT,
                                (Duration) null))));
  }

  @Test
  void publishToMqttShouldInvokeMqttServicePublishForEachStove() throws Exception {
    // GIVEN
    final var stoves = List.of(StoveId.of(1L), StoveId.of(2L), StoveId.of(3L));
    bridge.initStoves(stoves);
    when(rikaFirenetService.getStatus(any())).thenReturn(mock(StoveStatus.class));

    // WHEN
    bridge.publishToMqtt();

    // THEN
    verify(mqttPublicationService, times(stoves.size())).publish(any());
  }

  @Test
  void publishToMqttShouldInvokeRikaFirenetServiceGetStatusForEachStove() throws Exception {
    // GIVEN
    final var firstStove = StoveId.of(1L);
    final var secondStove = StoveId.of(2L);
    final var thirdStove = StoveId.of(3L);
    final var stoves = List.of(firstStove, secondStove, thirdStove);
    bridge.initStoves(stoves);
    when(rikaFirenetService.getStatus(any())).thenReturn(mock(StoveStatus.class));

    // WHEN
    bridge.publishToMqtt();

    // THEN
    verify(rikaFirenetService).getStatus(firstStove);
    verify(rikaFirenetService).getStatus(secondStove);
    verify(rikaFirenetService).getStatus(thirdStove);
  }

  @Test
  void publishToMqttShouldInvokeMqttServicePublishNotificationForEachStoveHavingAnError()
      throws Exception {
    // GIVEN
    final var stoves = List.of(StoveId.of(1L), StoveId.of(2L), StoveId.of(3L));
    bridge.initStoves(stoves);
    final var stoveStatus = mock(StoveStatus.class);
    when(stoveStatus.getError())
        .thenReturn(Optional.of(StoveError.builder().statusError(1).statusSubError(12).build()));

    final var enrichedError = mock(dev.cookiecode.rika2mqtt.plugins.api.v1.model.StoveError.class);
    when(enrichedError.getErrorCode()).thenReturn("42");
    when(stoveErrorMapper.toApiStoveError(anyLong(), any())).thenReturn(enrichedError);

    when(rikaFirenetService.getStatus(any())).thenReturn(stoveStatus);

    // WHEN
    bridge.publishToMqtt();

    // THEN
    verify(mqttPublicationService, times(stoves.size())).publishNotification(any());
  }

  @Test
  void publishToMqttShouldNotInvokeMqttServicePublishErrorForEachStoveHavingNoError()
      throws Exception {
    // GIVEN
    final var stoves = List.of(StoveId.of(1L), StoveId.of(2L), StoveId.of(3L));
    bridge.initStoves(stoves);
    final var stoveStatus = mock(StoveStatus.class);
    when(stoveStatus.getError()).thenReturn(Optional.empty());

    when(rikaFirenetService.getStatus(any())).thenReturn(stoveStatus);

    // WHEN
    bridge.publishToMqtt();

    // THEN
    verify(mqttPublicationService, never()).publishNotification(any());
  }

  @Test
  void onReceiveMqttCommandShouldInvokeUpdateControls() throws Exception {

    // GIVEN
    final var stoveId = 42L;
    final var fields = new HashMap<String, String>();
    final var event = mock(MqttCommandEvent.class);
    when(event.getStoveId()).thenReturn(stoveId);
    when(event.getProps()).thenReturn(fields);

    // WHEN
    bridge.onReceiveMqttCommand(event);

    // THEN
    verify(rikaFirenetService).updateControls(any(StoveId.class), any());
  }

  @Test
  void onReceiveMqttCommandShouldPrintALogMessageWhenItReceivesACommand(CapturedOutput output) {

    // GIVEN
    final var stoveId = 42L;
    final var fields = new HashMap<String, String>();
    final var event = mock(MqttCommandEvent.class);
    when(event.getStoveId()).thenReturn(stoveId);
    when(event.getProps()).thenReturn(fields);

    // WHEN
    bridge.onReceiveMqttCommand(event);

    // THEN
    await()
        .atMost(5, SECONDS)
        .untilAsserted(
            () ->
                assertTrue(
                    output.getAll().contains(format(RECEIVED_MQTT_COMMAND_FOR_STOVE_S, stoveId))));
  }
}
