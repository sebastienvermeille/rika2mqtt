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

import static dev.cookiecode.rika2mqtt.rika.firenet.model.StoveStatus.RIKA_NO_ERROR_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test class
 *
 * @author Sebastien Vermeille
 */
@ExtendWith(MockitoExtension.class)
class StoveStatusTest {

  @Mock Controls controls;

  @Mock Sensors sensors;

  private StoveStatus stoveStatus;

  @BeforeEach
  void setUp() {
    stoveStatus = StoveStatus.builder().controls(controls).sensors(sensors).build();
  }

  @Test
  void getErrorShouldReturnAnErrorGivenThereIsOne() {

    // GIVEN
    final var statusError = 42;
    final var statusSubError = 23;
    when(sensors.getStatusError()).thenReturn(statusError);
    when(sensors.getStatusSubError()).thenReturn(statusSubError);

    // WHEN
    final var error = stoveStatus.getError();

    // THEN
    final var expectedError =
        StoveError.builder().statusError(statusError).statusSubError(statusSubError).build();

    assertThat(error).isPresent().isEqualTo(Optional.of(expectedError));
  }

  @Test
  void getErrorShouldReturnAnErrorGivenThereIsOnlyErrorNoSubError() {

    // GIVEN
    final var statusError = 42;
    final var statusSubError = RIKA_NO_ERROR_VALUE;
    when(sensors.getStatusError()).thenReturn(statusError);
    when(sensors.getStatusSubError()).thenReturn(statusSubError);

    // WHEN
    final var error = stoveStatus.getError();

    // THEN
    final var expectedError =
        StoveError.builder().statusError(statusError).statusSubError(statusSubError).build();

    assertThat(error).isPresent().isEqualTo(Optional.of(expectedError));
  }

  @Test
  void getErrorShouldReturnNoErrorGivenThereIsNoMainError() {

    // GIVEN
    final var statusError = RIKA_NO_ERROR_VALUE;
    final var statusSubError = 42;
    when(sensors.getStatusError()).thenReturn(statusError);
    when(sensors.getStatusSubError()).thenReturn(statusSubError);

    // WHEN
    final var error = stoveStatus.getError();

    // THEN
    assertThat(error).isEmpty();
  }
}
