package ch.svermeille.rika.audit.configuration;

import ch.svermeille.rika.shared.RequireRestartWhenChanged;
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
@ConfigurationProperties(prefix = "audit")
@Data
@Validated
public class AuditConfigProperties {

  @RequireRestartWhenChanged
  @NonNull
  private String logsPath = "logs/";
}
