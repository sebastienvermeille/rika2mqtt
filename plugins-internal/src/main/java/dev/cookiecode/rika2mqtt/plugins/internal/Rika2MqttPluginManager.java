package dev.cookiecode.rika2mqtt.plugins.internal;

import dev.cookiecode.rika2mqtt.plugins.api.StoveStatusExtension;
import dev.cookiecode.rika2mqtt.plugins.internal.event.PolledStoveStatusEvent;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

// TODO: add javadoc
@Service
@RequiredArgsConstructor
@Flogger
public class Rika2MqttPluginManager {

  private PluginManager pluginManager;

  @PostConstruct
  void init() {
    pluginManager = new DefaultPluginManager();
  }

  public void start() {
    log.atInfo().log("Plugin manager starting ...");
    pluginManager.loadPlugins();
    pluginManager.startPlugins();
  }

  @EventListener
  public void handlePolledStoveStatusEvent(PolledStoveStatusEvent event) {
    System.out.println("HOOK FOR PLUGINS received a polled status event!");
    var extensions = pluginManager.getExtensions(StoveStatusExtension.class);

    if (extensions.isEmpty()) {

      log.atSevere().log();
      log.atSevere().log();
      log.atSevere().log();
      log.atSevere().log("NO EXTENSION POINT FOUND for StoveStatusExtension!");
      log.atSevere().log();
      log.atSevere().log();
      log.atSevere().log();
    }

    extensions.forEach(
        stoveStatusExtension -> {
          log.atInfo().log("INVOKE EXTENSION POINT");
          stoveStatusExtension.onPollStoveStatusSucceed(event.getStoveStatus());
        });
  }
}
