package ch.svermeille.rika.config.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * @author Sebastien Vermeille
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConfigurationChangeRequireRestartEvent extends ApplicationEvent {

  private final String message;

  public ConfigurationChangeRequireRestartEvent(final String message, final Object source) {
    super(source);
    this.message = message;
  }
}
