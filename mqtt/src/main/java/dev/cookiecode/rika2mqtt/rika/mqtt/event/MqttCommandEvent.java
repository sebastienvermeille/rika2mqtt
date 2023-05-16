package dev.cookiecode.rika2mqtt.rika.mqtt.event;

import lombok.Getter;
import lombok.ToString;

@ToString
public class MqttCommandEvent {

  @Getter
  private final String message;

  public MqttCommandEvent(String message) {
    this.message = message;
  }
}
