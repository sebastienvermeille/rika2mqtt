package ch.svermeille.rika.firenet.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Sebastien Vermeille
 */
@Builder
@Getter
@ToString
@EqualsAndHashCode
public class Auth {

  private final String email;
  private final String password;
}
