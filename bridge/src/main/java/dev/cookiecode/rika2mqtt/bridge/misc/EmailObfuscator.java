/*
 * The MIT License
 * Copyright © 2022 Sebastien Vermeille
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package dev.cookiecode.rika2mqtt.bridge.misc;

import lombok.NonNull;
import org.springframework.stereotype.Component;

/**
 * Obfuscate a little email address (to not print the whole one in logs and so on) It replaces:
 * me.email@gmail.com -> me****il@gmail.com
 *
 * @author Sebastien Vermeille
 * @implNote Highly inspired from https://stackoverflow.com/a/51007284
 */
@Component
public class EmailObfuscator {

  public String maskEmailAddress(@NonNull final String email) {
    final var mask = "*****";
    final int at = email.indexOf("@");
    if (at > 2) {
      final int maskLen = Math.min(Math.max(at / 2, 2), 4);
      final int start = (at - maskLen) / 2;
      return email.substring(0, start)
          + mask.substring(0, maskLen)
          + email.substring(start + maskLen);
    }
    return email;
  }
}
