package ch.svermeille.rika.config.listener;

import ch.svermeille.rika.audit.logging.AuditLogger;
import ch.svermeille.rika.audit.xml.Actions;
import ch.svermeille.rika.audit.xml.AuditedAction;
import ch.svermeille.rika.config.ConfigurationService;
import ch.svermeille.rika.config.event.ConfigurationChangeRequireRestartEvent;
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

  private final ConfigurationService configurationService;

  @EventListener
  public ConfigurationChangeRequireRestartEvent onConfigurationChanged(@NonNull final ConfigurationChangedEvent event) {
    final var diffs = event.getDiff();
    final var requireRestart = this.configurationService.getPropertiesRequiringRestartWhenChanged().stream().anyMatch(diffs.keySet()::contains);

    this.auditLogger.audit(AuditedAction.builder()
        .withAction(Actions.CHANGED_CONFIGURATION)
        .withProps(diffs)
        .build()
    );

    if(requireRestart) {
      return new ConfigurationChangeRequireRestartEvent(
          String.format(
              "Changes applied in current configuration require a restart. (changed properties: %s)",
              diffs.keySet()
                  .stream()
                  .filter(this.configurationService.getPropertiesRequiringRestartWhenChanged()::contains)
                  .toArray()
          ),
          event
      );
    } else {
      return null;
    }
  }

}
