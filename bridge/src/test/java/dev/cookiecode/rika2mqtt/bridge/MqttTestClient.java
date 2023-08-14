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
