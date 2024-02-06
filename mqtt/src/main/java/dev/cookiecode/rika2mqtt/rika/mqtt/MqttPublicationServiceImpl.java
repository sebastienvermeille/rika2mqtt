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
package dev.cookiecode.rika2mqtt.rika.mqtt;

import dev.cookiecode.rika2mqtt.rika.mqtt.configuration.MqttConfiguration;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author Sebastien Vermeille
 */
@Service
@RequiredArgsConstructor
@Flogger
public class MqttPublicationServiceImpl implements MqttPublicationService {

  @Qualifier("mqttConfiguration.MqttGateway")
  private final MqttConfiguration.MqttGateway mqttGateway;

  @Qualifier("mqttConfiguration.MqttNotificationGateway")
  private final MqttConfiguration.MqttGateway mqttNotificationGateway;

  @Override
  public void publish(@NonNull final String message) {
    log.atInfo().log("Publish to mqtt:\n%s", message);
    mqttGateway.sendToMqtt(message);
  }

  @Override
  public void publishNotification(@NonNull final String message) {
    log.atInfo().log("Publish notification to mqtt:\n%s", message);
    mqttNotificationGateway.sendToMqtt(message);
  }
}
