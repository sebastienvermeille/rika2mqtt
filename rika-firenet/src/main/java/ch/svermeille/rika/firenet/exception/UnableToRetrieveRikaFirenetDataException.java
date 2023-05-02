/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.svermeille.rika.firenet.exception;

/**
 * @author Sebastien Vermeille
 */
public class UnableToRetrieveRikaFirenetDataException extends RikaFirenetException {

  public UnableToRetrieveRikaFirenetDataException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
