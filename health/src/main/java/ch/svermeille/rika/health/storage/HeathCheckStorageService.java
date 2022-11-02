package ch.svermeille.rika.health.storage;

import ch.svermeille.rika.health.storage.model.HealthCheckResult;
import java.util.List;
import lombok.SneakyThrows;

/**
 * @author Sebastien Vermeille
 */
public interface HeathCheckStorageService {
  @SneakyThrows
  List<HealthCheckResult> getSavedResults();

  void saveHealthCheckResult(HealthCheckResult checkResult);
}
