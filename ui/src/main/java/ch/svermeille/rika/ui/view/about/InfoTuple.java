package ch.svermeille.rika.ui.view.about;

import lombok.Builder;
import lombok.Data;

/**
 * @author Sebastien Vermeille
 */
@Data
@Builder
public class InfoTuple {
  private String key;
  private String value;
}
