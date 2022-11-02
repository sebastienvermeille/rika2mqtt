package ch.svermeille.rika.health.storage.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Sebastien Vermeille
 */
@AllArgsConstructor
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class HealthCheckResult implements Comparable<HealthCheckResult> {

  private final String name;
  private final Status status;
  private final LocalDateTime lastExecuted;

  @Override
  public int compareTo(final HealthCheckResult healthCheckResult) {
    return getName().compareTo(healthCheckResult.getName());
  }
}
