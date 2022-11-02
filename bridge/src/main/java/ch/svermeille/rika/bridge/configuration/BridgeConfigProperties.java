package ch.svermeille.rika.bridge.configuration;

import ch.svermeille.rika.shared.RequireRestartWhenChanged;
import java.time.Duration;
import lombok.Data;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import top.code2life.config.DynamicConfig;

/**
 * @author Sebastien Vermeille
 */
@Configuration
@DynamicConfig
@ConfigurationProperties(prefix = "bridge")
@Data
@Validated
public class BridgeConfigProperties {

  @RequireRestartWhenChanged
  @NonNull
  private Duration reportInterval = Duration.ofSeconds(30);
}
