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
@SpringBootApplication(scanBasePackages = {"dev.cookiecode.rika2mqtt.*"})
public class Rika2MqttApplication {

  public static void main(String[] args) {
    System.setProperty("flogger.backend_factory",
        "com.google.common.flogger.backend.slf4j.Slf4jBackendFactory#getInstance");
    SpringApplication.run(Rika2MqttApplication.class, args);
  }
}
