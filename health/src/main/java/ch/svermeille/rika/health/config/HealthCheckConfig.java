package ch.svermeille.rika.health.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * @author Sebastien Vermeille
 */
@Configuration
@Component
public class HealthCheckConfig {

  @Bean(name = "healthChecksExecutor")
  public Executor threadPoolTaskExecutor() {
    return new ThreadPoolTaskExecutor();
  }
}
