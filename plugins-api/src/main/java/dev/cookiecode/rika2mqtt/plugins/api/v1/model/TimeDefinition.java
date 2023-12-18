package dev.cookiecode.rika2mqtt.plugins.api.v1.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/** Class to represent time schedules such as : 10:00 - 12:30 */
@Builder
@Getter
@EqualsAndHashCode
public class TimeDefinition {
  private final int hours;
  private final int minutes;

  private static final Double ONE_MINUTE_DURATION_IN_SECONDS = 60.0;

  @Override
  public String toString() {
    return String.format("%s:%02d", hours, minutes);
  }

  /** Helper method to return 9.5 given 9h30 */
  public Double asDecimal() {
    return this.getHours() + (this.getMinutes() / ONE_MINUTE_DURATION_IN_SECONDS);
  }
}
