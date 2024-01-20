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
package dev.cookiecode.rika2mqtt.rika.firenet.model;

import com.google.gson.annotations.SerializedName;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;

/**
 * @author Sebastien Vermeille
 */
@Data
@Builder
public class StoveStatus {
  static final int RIKA_NO_ERROR_VALUE = 0;

  private String name;

  @SerializedName(
      value = "stoveId",
      alternate = {"stoveID"}) // for coherence (the rest of the api is using camelCase)
  private Long stoveId;

  private Long lastSeenMinutes;
  private Long lastConfirmedRevision;
  private String oem;
  private String stoveType;
  private Sensors sensors;
  private Controls controls;

  public Optional<StoveError> getError() {
    final var statusError =
        Optional.ofNullable(sensors)
            .map(Sensors::getStatusError)
            .filter(value -> value > RIKA_NO_ERROR_VALUE);
    final var statusSubError =
        Optional.ofNullable(sensors)
            .map(Sensors::getStatusSubError)
            .filter(value -> value > RIKA_NO_ERROR_VALUE);
    return statusError.map(
        integer ->
            StoveError.builder()
                .statusError(integer)
                .statusSubError(statusSubError.orElse(RIKA_NO_ERROR_VALUE))
                .build());
  }
}
