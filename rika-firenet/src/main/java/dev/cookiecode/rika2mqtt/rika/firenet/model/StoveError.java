package dev.cookiecode.rika2mqtt.rika.firenet.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class StoveError {

  private final int statusError;
  private final int statusSubError;

  @Override
  public String toString() {
    return String.format("E%04d.%02d", statusError, statusSubError);
  }
}
