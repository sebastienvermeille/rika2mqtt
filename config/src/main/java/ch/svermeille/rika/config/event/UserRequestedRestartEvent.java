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
public class UserRequestedRestartEvent extends ApplicationEvent {

  private final String message;

  public UserRequestedRestartEvent(final String message, final Object source) {
    super(source);
    this.message = message;
  }
}
