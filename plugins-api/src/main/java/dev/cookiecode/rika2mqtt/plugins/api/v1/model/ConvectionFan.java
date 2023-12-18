package dev.cookiecode.rika2mqtt.plugins.api.v1.model;

import lombok.*;

@Data
@Builder
public class ConvectionFan {
  private int identifier;
  private boolean active;
  private int level;
  private int area;
}
