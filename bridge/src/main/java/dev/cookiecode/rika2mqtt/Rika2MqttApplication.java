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
