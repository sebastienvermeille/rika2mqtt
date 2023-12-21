package dev.cookiecode.rika2mqtt.plugins.api;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * An instance of this class is provided to plugins in their constructor. This class facilitates
 * communication with Rika2Mqtt and plugin manager.
 */
@Getter
@NoArgsConstructor
public class PluginContext {

  private String name = "test";
}
