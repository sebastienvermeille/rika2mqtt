/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package dev.cookiecode.rika2mqtt.rika.firenet.exception;

/**
 * @author Sebastien Vermeille
 */
public class CouldNotAuthenticateToRikaFirenetException extends RikaFirenetException {

  public CouldNotAuthenticateToRikaFirenetException(final String message) {
    super(message);
  }
}
