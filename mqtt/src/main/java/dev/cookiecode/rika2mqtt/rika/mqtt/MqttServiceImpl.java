/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package dev.cookiecode.rika2mqtt.rika.mqtt;

import dev.cookiecode.rika2mqtt.rika.mqtt.configuration.MqttConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

/**
 * @author Sebastien Vermeille
 */
@Service
@RequiredArgsConstructor
@Flogger
public class MqttServiceImpl implements MqttService {

  private final MqttConfiguration.MqttGateway mqttGateway;

  @Override
  public void publish(final String message) {
    log.atInfo().log("Publish %s to mqtt", message);
    mqttGateway.sendToMqtt(message);
  }


}
