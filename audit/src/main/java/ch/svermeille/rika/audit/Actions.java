package ch.svermeille.rika.audit;

import java.util.logging.Level;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Sebastien Vermeille
 */
@AllArgsConstructor
@Getter
public enum Actions {
  CHANGED_CONFIGURATION(Level.WARNING);
 
  private Level level;
}
