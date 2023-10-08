package dev.cookiecode.rika2mqtt.plugins.api;

import dev.cookiecode.rika2mqtt.plugins.api.model.StoveStatus;
import org.pf4j.ExtensionPoint;

public interface StoveStatusExtension extends ExtensionPoint {

  /**
   * RIKA stove status is regularly polled by Rika2Mqtt. Each time a scheduled poll succeed this
   * hook will be invoked and forwarded to plugins.
   *
   * @param stoveStatus the status retrieved from rika-firenet
   */
  void onPollStoveStatusSucceed(StoveStatus stoveStatus);
}
