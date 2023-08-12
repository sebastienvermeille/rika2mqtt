/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package dev.cookiecode.rika2mqtt.rika.mqtt;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import dev.cookiecode.rika2mqtt.rika.mqtt.configuration.MqttConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test class
 *
 * @author Sebastien Vermeille
 */
@ExtendWith(MockitoExtension.class)
class MqttServiceImplTest {

  @Mock MqttConfiguration.MqttGateway mqttGateway;

  @InjectMocks MqttServiceImpl mqttService;

  @Test
  void publishShouldForwardMessageToMqttGateway() {
    // GIVEN
    final var message = "some data";

    // WHEN
    mqttService.publish(message);

    // THEN
    verify(mqttGateway, times(1)).sendToMqtt(message);
  }
}
