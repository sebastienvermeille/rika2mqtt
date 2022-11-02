package ch.svermeille.rika.health.check;

import ch.svermeille.rika.health.storage.model.HealthCheckResult;
import java.time.Duration;
import java.util.concurrent.Future;

/**
 * @author Sebastien Vermeille
 */
public interface HealthCheck {

  default boolean isEnabled() {
    return true;
  }

  String getName();

  Duration getExecutionInterval();

  Future<HealthCheckResult> execute();
}
