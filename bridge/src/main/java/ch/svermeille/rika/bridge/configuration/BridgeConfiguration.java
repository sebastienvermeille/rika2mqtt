package ch.svermeille.rika.bridge.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sebastien Vermeille
 */
@Configuration
@EnableConfigurationProperties(BridgeConfigProperties.class)
public class BridgeConfiguration {

}
