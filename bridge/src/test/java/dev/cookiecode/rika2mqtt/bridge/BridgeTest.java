/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package dev.cookiecode.rika2mqtt.bridge;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import dev.cookiecode.rika2mqtt.bridge.misc.EmailObfuscator;
import dev.cookiecode.rika2mqtt.rika.firenet.RikaFirenetService;
import dev.cookiecode.rika2mqtt.rika.firenet.model.StoveId;
import dev.cookiecode.rika2mqtt.rika.mqtt.MqttService;
import dev.cookiecode.rika2mqtt.rika.mqtt.event.MqttCommandEvent;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test class
 *
 * @author Sebastien Vermeille
 */
@ExtendWith(MockitoExtension.class)
class BridgeTest {

  @Mock
  RikaFirenetService rikaFirenetService;
  @Mock
  MqttService mqttService;
  @Mock
  EmailObfuscator emailObfuscator;
  @Mock
  Gson gson;
  @InjectMocks
  @Spy
  Bridge bridge;

  @Test
  void initShouldInitStovesWithRetrieveStovesFromRikaFirenet(){
    // GIVEN
    // nothing special

    // WHEN
    bridge.init();

    // THEN
    verify(rikaFirenetService, times(1)).getStoves();
    verify(bridge, times(1)).initStoves(anyList());
  }

  @Test
  void initShouldPrintStartupMessages(){
    // GIVEN
    // nothing special

    // WHEN
    bridge.init();

    // THEN
    verify(bridge, times(1)).printStartupMessages();
  }

  @Test
  void printStartupMessagesShouldObfuscateEmail(){
    // GIVEN
    // nothing special

    // WHEN
    bridge.printStartupMessages();

    // THEN
    verify(emailObfuscator, times(1)).maskEmailAddress(any());
  }

  @Test
  void publishToMqttShouldInvokeMqttServicePublishForEachStove() {
    // GIVEN
    final var stoves = List.of(
        StoveId.of(1L),
        StoveId.of(2L),
        StoveId.of(3L)
    );
    bridge.initStoves(stoves);

    // WHEN
    bridge.publishToMqtt();

    // THEN
    verify(mqttService, times(stoves.size())).publish(any());
  }

  @Test
  void publishToMqttShouldInvokeRikaFirenetServiceGetStatusForEachStove() throws Exception {
    // GIVEN
    final var firstStove = StoveId.of(1L);
    final var secondStove = StoveId.of(2L);
    final var thirdStove = StoveId.of(3L);
    final var stoves = List.of(
      firstStove,
      secondStove,
      thirdStove
    );
    bridge.initStoves(stoves);

    // WHEN
    bridge.publishToMqtt();

    // THEN
    verify(rikaFirenetService).getStatus(firstStove);
    verify(rikaFirenetService).getStatus(secondStove);
    verify(rikaFirenetService).getStatus(thirdStove);
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
}