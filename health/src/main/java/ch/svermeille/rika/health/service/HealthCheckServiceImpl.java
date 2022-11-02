package ch.svermeille.rika.health.service;

import static java.time.ZoneOffset.UTC;
import static java.util.concurrent.TimeUnit.SECONDS;

import ch.svermeille.rika.health.check.HealthCheck;
import ch.svermeille.rika.health.storage.HeathCheckStorageService;
import ch.svermeille.rika.health.storage.model.HealthCheckResult;
import ch.svermeille.rika.health.storage.model.Status;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;

/**
 * @author Sebastien Vermeille
 */
@Service
@RequiredArgsConstructor
@Flogger
public class HealthCheckServiceImpl implements HealthCheckService, InitializingBean {

  private final List<HealthCheck> healthChecks;

  private final HeathCheckStorageService storageService;

  private final TaskScheduler taskScheduler;

  @Override
  public void afterPropertiesSet() {

    this.healthChecks.stream()
        .filter(HealthCheck::isEnabled)
        .forEach(healthCheck -> {

          this.storageService.saveHealthCheckResult(
              HealthCheckResult.builder()
                  .name(healthCheck.getName())
                  .lastExecuted(LocalDateTime.now(UTC))
                  .status(Status.SCHEDULED)
                  .build()
          );

          final var periodicTrigger = new PeriodicTrigger(healthCheck.getExecutionInterval().toSeconds(), SECONDS);
          this.taskScheduler.schedule(() -> {
            final var inProgress = HealthCheckResult.builder()
                .name(healthCheck.getName())
                .status(Status.IN_PROGRESS)
                .lastExecuted(LocalDateTime.now(UTC))
                .build();
            this.storageService.saveHealthCheckResult(inProgress);
            final var result = healthCheck.execute();
            try {
              final var response = result.get(healthCheck.getExecutionInterval().plusSeconds(60).toSeconds(), SECONDS);
              this.storageService.saveHealthCheckResult(response);
            } catch(final Exception e) {
              final var error = HealthCheckResult.builder()
                  .name(healthCheck.getName())
                  .status(Status.FAILED)
                  .lastExecuted(LocalDateTime.now(UTC))
                  .build();
              this.storageService.saveHealthCheckResult(error);
              if(e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
              }
            }
          }, periodicTrigger);
        });

  }

  @Override
  public List<HealthCheckResult> getHealthCheckResults() {
    return this.storageService.getSavedResults();
  }


}
