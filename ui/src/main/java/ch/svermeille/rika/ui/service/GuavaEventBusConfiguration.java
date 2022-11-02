package ch.svermeille.rika.ui.service;

import com.google.common.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sebastien Vermeille
 */
@Configuration
public class GuavaEventBusConfiguration {
  //  @SessionScoped
  @Bean(name = "eventBus")
  public EventBus eventBus() {
    return new EventBus();
  }
}
