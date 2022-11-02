package ch.svermeille.rika.firenet.health.check;

import static ch.svermeille.rika.health.service.PingUtils.pingUrl;
import static java.time.LocalDateTime.now;
import static java.time.ZoneOffset.UTC;

import ch.svermeille.rika.firenet.RikaFirenetService;
import ch.svermeille.rika.health.check.HealthCheck;
import ch.svermeille.rika.health.storage.model.HealthCheckResult;
import ch.svermeille.rika.health.storage.model.Status;
import java.time.Duration;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

/**
 * @author Sebastien Vermeille
 */
@Component
@RequiredArgsConstructor
@Flogger
public class RikaFirenetConnectivityCheckImpl implements HealthCheck {

  static final String RIKA_FIRENET_CONNECTIVITY_CHECK = "Check RIKA firenet connectivity";

  private final RikaFirenetService rikaFirenetService;

  @Override
  public String getName() {
    return RIKA_FIRENET_CONNECTIVITY_CHECK;
  }

  @Override
  public Duration getExecutionInterval() {
    return Duration.ofMinutes(30);
  }

  @Override
  @Async("healthChecksExecutor")
  public Future<HealthCheckResult> execute() {

    Status status = null;
    if(isRikaFirenetServiceReachable() && isAuthenticated() && hasStoves()) {
      status = Status.PASS;
    } else {
      status = Status.FAILED;
    }

    final var res = HealthCheckResult.builder()
        .lastExecuted(now(UTC))
        .name(RIKA_FIRENET_CONNECTIVITY_CHECK)
        .status(status)
        .build();

    return new AsyncResult<>(res);
  }

  private boolean isRikaFirenetServiceReachable() {
    return pingUrl("https://www.rika-firenet.com/", Duration.ofSeconds(5));
  }

  private boolean isAuthenticated() {
    return this.rikaFirenetService.isAuthenticated();
  }

  private boolean hasStoves() {
    return !this.rikaFirenetService.getStoves().isEmpty();
  }

}
