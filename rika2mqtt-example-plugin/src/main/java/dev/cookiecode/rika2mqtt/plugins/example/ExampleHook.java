package dev.cookiecode.rika2mqtt.plugins.example;

import dev.cookiecode.rika2mqtt.plugins.api.v1.StoveStatusExtension;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.StoveStatus;
import org.pf4j.Extension;

@Extension
public class ExampleHook implements StoveStatusExtension {

  @Override
  public void onPollStoveStatusSucceed(StoveStatus stoveStatus) {
    System.out.println("EXAMPLE PLUGIN >> onPollStoveStatusSucceed invoked");
  }
}
