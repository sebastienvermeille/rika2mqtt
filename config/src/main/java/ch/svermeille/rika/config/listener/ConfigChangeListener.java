package ch.svermeille.rika.config.listener;

import ch.svermeille.rika.audit.Actions;
import ch.svermeille.rika.audit.AuditLogger;
import ch.svermeille.rika.audit.AuditedAction;
import ch.svermeille.rika.config.event.ConfigurationChangeRequireRestartEvent;
import java.util.Set;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import top.code2life.config.ConfigurationChangedEvent;

/**
 * @author Sebastien Vermeille
 */
@Component
@RequiredArgsConstructor
@Flogger
public class ConfigChangeListener {

  private final AuditLogger auditLogger;
  static final Set<String> PROPS_REQUIRE_RESTART = Set.of(
      "bridge.reportInterval",
      "rika.email",
      "rika.password",
      "rika.keepAliveTimeout",
      "mqtt.host",
      "mqtt.port",
      "mqtt.user",
      "mqtt.password",
      "mqtt.clientName",
      "mqtt.telemetryReportTopicName",
      "mqtt.commandTopicName" // TODO: see if we cannot have a way to either annotate properties as @RequireRestart or @FieldNameConstants
  );

  @EventListener
  public ConfigurationChangeRequireRestartEvent onConfigurationChanged(@NonNull ConfigurationChangedEvent event) {
    final var diffs = event.getDiff();
    final var requireRestart = PROPS_REQUIRE_RESTART.stream().anyMatch(diffs.keySet()::contains);

    auditLogger.audit(AuditedAction.builder()
        .withAction(Actions.CHANGED_CONFIGURATION)
        .withProps(diffs)
        .build()
    );

    if(requireRestart){
      log.atInfo().log();
      return new ConfigurationChangeRequireRestartEvent(
          String.format(
              "Changes applied in current configuration require a restart. (changed properties: %s)",
              diffs.keySet()
                  .stream()
                  .filter(PROPS_REQUIRE_RESTART::contains)
                  .toArray()
          ),
          event
      );
    }
    else {
      return null;
    }
  }

}
