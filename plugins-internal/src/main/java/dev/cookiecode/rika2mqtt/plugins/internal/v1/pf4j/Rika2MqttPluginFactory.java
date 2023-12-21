package dev.cookiecode.rika2mqtt.plugins.internal.v1.pf4j;

import dev.cookiecode.rika2mqtt.plugins.api.PluginContext;
import lombok.extern.flogger.Flogger;
import org.pf4j.DefaultPluginFactory;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

@Flogger
public class Rika2MqttPluginFactory extends DefaultPluginFactory {

  @Override
  protected Plugin createInstance(Class<?> pluginClass, PluginWrapper pluginWrapper) {

    final var pluginContext = new PluginContext();
    try {
      final var constructor = pluginClass.getConstructor(PluginContext.class);
      return (Plugin) constructor.newInstance(pluginContext);
    } catch (Exception e) {
      log.atSevere().withCause(e).log(e.getMessage());
      return null;
    }
  }
}
