/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
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


  @Autowired
  private MqttService mqttService;

  @Autowired
  private MqttConfigProperties mqttConfigProperties;

  @BeforeEach
  void setup() {
  }

  @Test
  void publishMqttMessageFromBridgeShouldEffectivelyPublishAMessageToMqtt() {
    String message = "some apples";

    // Here, another mqtt client connect to the telemetry topic
    // after using the bridge mqttService to publish to mqtt,
    // the MQTT test client (outside the rika2mqtt bridge) should be able to receive that message
    getMqttTestClient().assertThatMessageWasPublishedToMqttTopic(
        message,
        mqttConfigProperties.getTelemetryReportTopicName(),
        () -> mqttService.publish(message)
    );
  }

//  @Test
//  void receiveMqttCommandMessageShouldNotGenerateAnyException()
//      throws MqttException {
//
//// TODO: refactore and provide convenient assert methods/framework kit for integrations tests
//    List<String> receivedAnswers = new ArrayList<>();
//
//    var subscriber = getRandomMqttClient();
//    subscriber.subscribe(mqttConfigProperties.getCommandTopicName(), (topic, msg) -> {
//      System.out.println("Received message ...");
//      receivedAnswers.add(msg.toString());
//    });
//
//    var message = """
//        {
//           "stoveId": 43,
//           "targetTemperature": 12
//        }
//        """;
//
//    var mqttClient = getRandomMqttClient();
//    MqttMessage msg = new MqttMessage();
//    msg.setQos(1);
//    msg.setPayload(message.getBytes(StandardCharsets.UTF_8));
//    mqttClient.publish(mqttConfigProperties.getCommandTopicName(), msg);
//
////
////    assertTimeout(Duration.ofSeconds(15), () -> {
////      assertFalse(receivedAnswers.isEmpty());
////      assertTrue(receivedAnswers.contains(message));
////      subscriber.disconnectForcibly();
////
////    });
//
//  }

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
