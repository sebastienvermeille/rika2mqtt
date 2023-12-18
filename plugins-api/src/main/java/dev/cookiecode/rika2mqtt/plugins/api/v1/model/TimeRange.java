package dev.cookiecode.rika2mqtt.plugins.api.v1.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class TimeRange {
  private final TimeDefinition from;
  private final TimeDefinition to;

  @Override
  public String toString() {
    return String.format("%s - %s", from.toString(), to.toString());
  }
}
