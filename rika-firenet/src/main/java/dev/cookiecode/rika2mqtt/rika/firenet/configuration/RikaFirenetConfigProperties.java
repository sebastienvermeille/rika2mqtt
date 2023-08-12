/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package dev.cookiecode.rika2mqtt.rika.firenet.configuration;

import jakarta.validation.constraints.NotEmpty;
import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * @author Sebastien Vermeille
 */
@Configuration
@ConfigurationProperties(prefix = "rika")
@Data
@Validated
public class RikaFirenetConfigProperties {

  @NotEmpty private String email;

  @NotEmpty private String password;

  private Duration keepAliveTimeout = Duration.ofSeconds(60); // perform a login after timeout
  // occurs if no query were executed within period
}
