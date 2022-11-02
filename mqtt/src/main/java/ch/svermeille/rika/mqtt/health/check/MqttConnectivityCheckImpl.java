package ch.svermeille.rika.mqtt.health.check;

import static ch.svermeille.rika.health.storage.model.Status.FAILED;
import static ch.svermeille.rika.health.storage.model.Status.PASS;
import static java.time.LocalDateTime.now;
import static java.time.ZoneOffset.UTC;

import ch.svermeille.rika.health.check.HealthCheck;
import ch.svermeille.rika.health.service.PingUtils;
import ch.svermeille.rika.health.storage.model.HealthCheckResult;
import ch.svermeille.rika.mqtt.configuration.MqttConfigProperties;
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
public class MqttConnectivityCheckImpl implements HealthCheck, Runnable {

  private static final String MQTT_CONNECTIVITY_CHECK = "Check Mqtt connectivity";

  private final MqttConfigProperties mqttConfigProperties;
  private final PingUtils pingUtils;

  @Override
  public String getName() {
    return MQTT_CONNECTIVITY_CHECK;
  }

  @Override
  public Duration getExecutionInterval() {
    return Duration.ofSeconds(30);
  }

  @Override
  @Async("healthChecksExecutor")
  public Future<HealthCheckResult> execute() {

    var status = FAILED;
    if(isHostReachable()) {
      status = PASS;
    }

    final var res = HealthCheckResult.builder()
        .lastExecuted(now(UTC))
        .name(MQTT_CONNECTIVITY_CHECK)
        .status(status)
        .build();

    return new AsyncResult<>(res);
  }

  private boolean isHostReachable() {
    return this.pingUtils.pingHost(this.mqttConfigProperties.getHost(), this.mqttConfigProperties.getPort(), Duration.ofSeconds(10));
  }


  @Override
  public void run() {
    execute();
  }
}
