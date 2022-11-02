package ch.svermeille.rika.health.check;

import static java.time.LocalDateTime.now;
import static java.time.ZoneOffset.UTC;

import ch.svermeille.rika.health.service.MavenCentralSearchService;
import ch.svermeille.rika.health.storage.model.HealthCheckResult;
import ch.svermeille.rika.health.storage.model.Status;
import java.time.Duration;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.springframework.boot.info.BuildProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

/**
 * @author Sebastien Vermeille
 */
@Component
@RequiredArgsConstructor
@Flogger
public class UpToDateHealthCheckImpl implements HealthCheck {

  private static final String UP_TO_DATE_HEALTH_CHECK = "Check version up to date";
  private static final String GROUP_ID = "dev.cookiecode";
  private static final String ARTIFACT_ID = "protobuf-maven-plugin";

  private final BuildProperties buildProperties;
  private final MavenCentralSearchService mavenCentralSearchService;

  @Override
  public String getName() {
    return UP_TO_DATE_HEALTH_CHECK;
  }

  @Override
  public Duration getExecutionInterval() {
    return Duration.ofHours(12);
  }

  @Override
  @Async("healthChecksExecutor")
  public Future<HealthCheckResult> execute() {
    final var currentVersion = new DefaultArtifactVersion(this.buildProperties.getVersion());
    final var lastVersion = new DefaultArtifactVersion(this.mavenCentralSearchService.getLatestVersion(GROUP_ID, ARTIFACT_ID));

    var status = Status.PASS;

    if(currentVersion.compareTo(lastVersion) < 0) {
      log.atWarning()
          .log("Current version %s it outdated. Please upgrade to %s to benefit from last improvements.", currentVersion, lastVersion);
      status = Status.FAILED;
    }
    final var res = HealthCheckResult.builder()
        .lastExecuted(now(UTC))
        .name(UP_TO_DATE_HEALTH_CHECK)
        .status(status)
        .build();

    return new AsyncResult<>(res);
  }


}
