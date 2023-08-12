/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package dev.cookiecode.rika2mqtt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Sebastien Vermeille
 */
@SpringBootApplication(scanBasePackages = {Rika2MqttApplication.RIKA2MQTT_BASE_PACKAGE})
public class Rika2MqttApplication {

  static final String RIKA2MQTT_BASE_PACKAGE = "dev.cookiecode.rika2mqtt.*";
  private static final String FLOGGER_BACKEND_FACTORY_PROPERTY = "flogger.backend_factory";
  private static final String SLF4J_BACKEND_FACTORY =
      "com.google.common.flogger.backend.slf4j.Slf4jBackendFactory#getInstance";

  public static void main(String[] args) {
    System.setProperty(FLOGGER_BACKEND_FACTORY_PROPERTY, SLF4J_BACKEND_FACTORY);
    SpringApplication.run(Rika2MqttApplication.class, args);
  }
}
