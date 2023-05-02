package ch.svermeille.rika.firenet.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Sebastien Vermeille
 */
@Configuration
@EnableScheduling
@EnableConfigurationProperties(RikaFirenetConfigProperties.class)
public class RikaFirenetConfiguration
{
}
