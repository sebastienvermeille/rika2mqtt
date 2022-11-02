package ch.svermeille.rika.health.service;

import ch.svermeille.rika.health.storage.model.HealthCheckResult;
import java.util.List;

/**
 * @author Sebastien Vermeille
 */
public interface HealthCheckService {

  List<HealthCheckResult> getHealthCheckResults();
}
