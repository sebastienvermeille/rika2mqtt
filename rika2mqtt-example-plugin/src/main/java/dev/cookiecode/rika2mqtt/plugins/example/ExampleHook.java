package dev.cookiecode.rika2mqtt.plugins.example;

import dev.cookiecode.rika2mqtt.plugins.api.StoveStatusExtension;
import dev.cookiecode.rika2mqtt.plugins.api.model.StoveStatus;
import org.pf4j.Extension;

@Extension
public class ExampleHook implements StoveStatusExtension {

  @Override
  public void onPollStoveStatusSucceed(StoveStatus stoveStatus) {
    System.out.println("EXAMPLE PLUGIN >> onPollStoveStatusSucceed invoked");
  }
}
