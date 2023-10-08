package dev.cookiecode.rika2mqtt.plugins.example;

import dev.cookiecode.rika2mqtt.plugins.api.Rika2MqttPlugin;

public class ExamplePlugin extends Rika2MqttPlugin {

  ExampleHook hook;

  @Override
  public void start() {
    System.out.println("EXAMPLE PLUGIN >> STARTED");
    hook = new ExampleHook();
  }

  @Override
  public void stop() {
    System.out.println("EXAMPLE PLUGIN >> STOPPED");
  }
}
