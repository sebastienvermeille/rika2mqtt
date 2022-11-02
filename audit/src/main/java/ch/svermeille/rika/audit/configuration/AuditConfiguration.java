package ch.svermeille.rika.audit.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sebastien Vermeille
 */
@Configuration
@EnableConfigurationProperties(AuditConfigProperties.class)
public class AuditConfiguration {
}
