package ch.svermeille.rika.firenet.configuration;

import static java.time.Duration.ofSeconds;

import ch.svermeille.rika.shared.RequireRestartWhenChanged;
import java.time.Duration;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import top.code2life.config.DynamicConfig;

/**
 * @author Sebastien Vermeille
 */
@Configuration
@DynamicConfig
@ConfigurationProperties(prefix = "rika")
@Data
@Validated
public class RikaFirenetConfigProperties {

  @NotEmpty
  @RequireRestartWhenChanged
  private String email;

  @NotEmpty
  @RequireRestartWhenChanged
  private String password;

  @RequireRestartWhenChanged
  private Duration keepAliveTimeout = ofSeconds(60); // perform a login after timeout occurs if no query were executed within period
}
