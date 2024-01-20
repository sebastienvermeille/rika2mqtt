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

import com.google.gson.Gson;
import dev.cookiecode.rika2mqtt.rika.mqtt.MqttService;
import dev.cookiecode.rika2mqtt.rika.mqtt.MqttServiceImpl;
import dev.cookiecode.rika2mqtt.rika.mqtt.configuration.MqttConfigProperties;
import dev.cookiecode.rika2mqtt.rika.mqtt.configuration.MqttConfiguration;
import java.util.UUID;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test class
 *
 * @author Sebastien Vermeille
 */
@SpringBootTest(classes = {MqttServiceImpl.class, MqttConfiguration.class, Gson.class})
@ActiveProfiles("test")
class BridgeIntegrationTest extends AbstractBaseIntegrationTest {

  @Autowired private MqttService mqttService;

  @Autowired private MqttConfigProperties mqttConfigProperties;

  @BeforeEach
  void setup() {}

  @Test
  void publishMqttMessageFromBridgeShouldEffectivelyPublishAMessageToMqtt() {
    String message = "some apples";

    // Here, another mqtt client connect to the telemetry topic
    // after using the bridge mqttService to publish to mqtt,
    // the MQTT test client (outside the rika2mqtt bridge) should be able to receive that message
    getMqttTestClient()
        .assertThatMessageWasPublishedToMqttTopic(
            message,
            mqttConfigProperties.getTelemetryReportTopicName(),
            () -> mqttService.publish(message));
  }

  @Test
  void publishErrorMqttMessageFromBridgeShouldEffectivelyPublishAnErrorMessageToMqtt() {
    String message = "some error";

    // Here, another mqtt client connect to the telemetry topic
    // after using the bridge mqttService to publish to mqtt,
    // the MQTT test client (outside the rika2mqtt bridge) should be able to receive that message
    getMqttTestClient()
        .assertThatMessageWasPublishedToMqttTopic(
            message,
            mqttConfigProperties.getNotificationTopicName(),
            () -> mqttService.publishError(message));
  }

  private MqttTestClient getMqttTestClient() {
    try {
      var client = getRandomMqttClient();
      return new MqttTestClient(client);
    } catch (MqttException e) {
      throw new RuntimeException(e);
    }
  }

  private MqttClient getRandomMqttClient() throws MqttException {
    String subscriberId = UUID.randomUUID().toString();

    final var client =
        new MqttClient(
            "tcp://" + mqttConfigProperties.getHost() + ":" + mqttConfigProperties.getPort(),
            subscriberId);

    MqttConnectOptions options = new MqttConnectOptions();
    options.setAutomaticReconnect(true);
    options.setCleanSession(true);
    options.setConnectionTimeout(10);
    options.setUserName(mqttConfigProperties.getUsername());
    options.setPassword(mqttConfigProperties.getPassword().toCharArray());
    client.connect(options);

    return client;
  }
}
