/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.svermeille.rika.bridge;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertTimeout;

import ch.svermeille.rika.mqtt.MqttService;
import ch.svermeille.rika.mqtt.MqttServiceImpl;
import ch.svermeille.rika.mqtt.configuration.MqttConfigProperties;
import ch.svermeille.rika.mqtt.configuration.MqttConfiguration;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(classes = {MqttServiceImpl.class, MqttConfiguration.class})
@ActiveProfiles("test")
public class BridgeIntegrationTest extends AbstractBaseIntegrationTest {


  @Autowired
  private MqttService mqttService;

  @Autowired
  private MqttConfigProperties mqttConfigProperties;

  @BeforeEach
  void setup() {
  }

  @Test
  public void publishMqttMessageShouldNotGenerateAnyException()
      throws MqttException {
// TODO: refactore and provide convenient assert methods/framework kit for integrations tests
    List<String> receivedAnswers = new ArrayList<>();

    var subscriber = getRandomMqttClient();
    subscriber.subscribe(mqttConfigProperties.getTelemetryReportTopicName(), (topic, msg) -> {
      receivedAnswers.add(msg.toString());
    });

    mqttService.publish("test");

    assertTimeout(Duration.ofSeconds(5), () -> {
      assertFalse(receivedAnswers.isEmpty());
      assertTrue(receivedAnswers.contains("test"));
    });
  }

  private MqttClient getRandomMqttClient() throws MqttException {
    String subscriberId = UUID.randomUUID().toString();

    final var client = new MqttClient(
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
