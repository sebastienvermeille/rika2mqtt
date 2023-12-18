package dev.cookiecode.rika2mqtt.plugins.api.v1.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParameterErrorCount {

  private int number;
  private int value;
}
