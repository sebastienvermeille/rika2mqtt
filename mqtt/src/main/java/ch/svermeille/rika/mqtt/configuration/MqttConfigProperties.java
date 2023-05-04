/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.svermeille.rika.mqtt.configuration;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * @author Sebastien Vermeille
 */
@Configuration
@ConfigurationProperties(prefix = "mqtt")
@Data
@Validated
public class MqttConfigProperties {

  @NotEmpty
  private String host;

  @NotNull
  private Integer port = 1883;

  private String username;

  private String password;

  private String clientName = "rika2mqtt";

  private String telemetryReportTopicName = "tele/rika2mqtt";

  private String commandTopicName = "cmnd/rika2mqtt";
}
