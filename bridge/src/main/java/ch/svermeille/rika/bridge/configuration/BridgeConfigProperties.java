/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.svermeille.rika.bridge.configuration;

import java.time.Duration;
import lombok.Data;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * @author Sebastien Vermeille
 */
@Configuration
@ConfigurationProperties(prefix = "bridge")
@Data
@Validated
public class BridgeConfigProperties {

  @NonNull
  private Duration reportInterval = Duration.ofSeconds(30);
}
