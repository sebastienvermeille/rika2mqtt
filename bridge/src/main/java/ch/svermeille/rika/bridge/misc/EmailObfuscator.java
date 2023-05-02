package ch.svermeille.rika.bridge.misc;

import lombok.NonNull;
import org.springframework.stereotype.Component;

/**
 * Obfuscate a little email address (to not print the whole one in logs and so on)
 * It replaces: me.email@gmail.com -> me****il@gmail.com
 *
 * @author Sebastien Vermeille
 * @implNote Highly inspired from https://stackoverflow.com/a/51007284
 */
@Component
public class EmailObfuscator {

  public String maskEmailAddress(@NonNull final String email) {
    final var mask = "*****";
    final int at = email.indexOf("@");
    if(at > 2) {
      final int maskLen = Math.min(Math.max(at / 2, 2), 4);
      final int start = (at - maskLen) / 2;
      return email.substring(0, start) + mask.substring(0, maskLen) + email.substring(start + maskLen);
    }
    return email;
  }

}
