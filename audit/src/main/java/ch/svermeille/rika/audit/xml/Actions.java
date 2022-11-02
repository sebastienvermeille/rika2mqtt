package ch.svermeille.rika.audit.xml;

import java.util.logging.Level;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Sebastien Vermeille
 */
@AllArgsConstructor
@Getter
public enum Actions {
  CHANGED_CONFIGURATION(Level.WARNING),
  USER_RESTART_APPLICATION(Level.INFO);

  private final Level level;
}
