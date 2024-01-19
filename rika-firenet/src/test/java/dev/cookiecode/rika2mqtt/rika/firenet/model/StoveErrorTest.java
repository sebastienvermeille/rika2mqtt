package dev.cookiecode.rika2mqtt.rika.firenet.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Test class
 *
 * @author Sebastien Vermeille
 */
class StoveErrorTest {

  @Test
  void toStringShouldPrependDigitsInFrontOfSmallErrorCodes() {
    // GIVEN
    final var error = StoveError.builder().statusError(1).statusSubError(2).build();

    // WHEN
    final var result = error.toString();

    // THEN
    assertThat(result).isEqualTo("E0001.02");
  }

  @Test
  void toStringShouldReplacePrependingZeroWhenHavingBigErrorCodes() {
    // GIVEN
    final var error = StoveError.builder().statusError(1000).statusSubError(23).build();

    // WHEN
    final var result = error.toString();

    // THEN
    assertThat(result).isEqualTo("E1000.23");
  }
}
