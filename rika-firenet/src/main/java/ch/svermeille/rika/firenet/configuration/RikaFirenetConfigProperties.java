package ch.svermeille.rika.firenet.configuration;

import java.time.Duration;
import javax.validation.constraints.NotEmpty;
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

  @NotEmpty
  private String email;

  @NotEmpty
  private String password;

  private Duration keepAliveTimeout = Duration.ofSeconds(60); // perform a login after timeout
  // occurs if no query were executed within period
}
