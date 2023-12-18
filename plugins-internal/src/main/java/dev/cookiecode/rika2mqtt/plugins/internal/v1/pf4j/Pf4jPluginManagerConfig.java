package dev.cookiecode.rika2mqtt.plugins.internal.v1.pf4j;

import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Pf4jPluginManagerConfig {

  @Bean
  public PluginManager pluginManager() {
    return new DefaultPluginManager();
  }
}
