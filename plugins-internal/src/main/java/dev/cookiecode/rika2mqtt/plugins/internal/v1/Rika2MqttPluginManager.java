package dev.cookiecode.rika2mqtt.plugins.internal.v1;

import dev.cookiecode.rika2mqtt.plugins.api.v1.StoveStatusExtension;
import dev.cookiecode.rika2mqtt.plugins.internal.v1.event.PolledStoveStatusEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.pf4j.PluginManager;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Manage plugin loading, start etc.
 *
 * @author Sebastien Vermeille
 */
@Service
@RequiredArgsConstructor
@Flogger
public class Rika2MqttPluginManager {

  private final PluginManager pluginManager;

  public void start() {
    log.atInfo().log("Plugin manager starting ...");
    pluginManager.loadPlugins();
    pluginManager.startPlugins();
  }

  @EventListener
  public void handlePolledStoveStatusEvent(PolledStoveStatusEvent event) {
    var extensions = pluginManager.getExtensions(StoveStatusExtension.class);

    if (extensions.isEmpty()) {
      log.atFinest().log(
          "None of the %s plugin(s) registered a hook for extension %s, not forwarding stove status.",
          pluginManager.getPlugins().size(), StoveStatusExtension.class.getSimpleName());
    } else {
      extensions.forEach(
          stoveStatusExtension ->
              stoveStatusExtension.onPollStoveStatusSucceed(event.getStoveStatus()));
    }
  }
}
