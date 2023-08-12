/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package dev.cookiecode.rika2mqtt.bridge;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttTestClient {

  private final MqttClient mqttClient;

  public MqttTestClient(MqttClient mqttClient) {
    this.mqttClient = mqttClient;
  }

  public void assertThatMessageWasPublishedToMqttTopic(
      String message, String mqttTopic, Runnable runnable) {
    try {
      final List<String> receivedAnswers = new ArrayList<>();

      System.out.println("Waiting for MQTT test client to be connected");
      Awaitility.with()
          .atMost(30, TimeUnit.SECONDS)
          .pollDelay(1000, TimeUnit.MILLISECONDS)
          .await()
          .until(mqttClient::isConnected);
      System.out.println("connected!");

      mqttClient.subscribe(
          mqttTopic,
          (topic, msg) -> {
            System.out.println(
                String.format("Received message on %s: \n %s", topic, msg.toString()));
            receivedAnswers.add(msg.toString());
          });

      System.out.println("execute the runnable:");
      runnable.run();
      System.out.println("done");

      Awaitility.with()
          .atMost(30, TimeUnit.SECONDS)
          .pollDelay(1000, TimeUnit.MILLISECONDS)
          .await()
          .until(() -> receivedAnswers.size() > 0);

      System.out.println("check assertions:");
      assertTrue(receivedAnswers.contains(message));
      mqttClient.unsubscribe(mqttTopic);
      mqttClient.disconnect();
    } catch (MqttException e) {
      throw new RuntimeException(e);
    }
  }
}
