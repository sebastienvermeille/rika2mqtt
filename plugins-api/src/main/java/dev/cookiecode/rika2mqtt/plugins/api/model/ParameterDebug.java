package dev.cookiecode.rika2mqtt.plugins.api.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParameterDebug {

  private int number;
  private int value;
}
