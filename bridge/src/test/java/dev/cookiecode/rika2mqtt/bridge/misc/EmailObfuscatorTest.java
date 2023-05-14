/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package dev.cookiecode.rika2mqtt.bridge.misc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class
 *
 * @author Sebastien Vermeille
 */
class EmailObfuscatorTest {

  private EmailObfuscator obfuscator;

  @BeforeEach
  void setUp(){
    obfuscator = new EmailObfuscator();
  }

  @Test
  void maskEmailAddressShouldReplaceAFewCharactersByStarsGivenAnEmailAddress(){
    // GIVEN
    final var email = "jean.marc@gmail.com";

    // WHEN
    final var obfuscatedEmail = obfuscator.maskEmailAddress(email);

    // THEN
    assertThat(obfuscatedEmail)
        .contains("*")
        .isNotEqualTo(email)
        .isEqualTo("je****arc@gmail.com");
  }

  @Test
  void maskEmailAddressShouldReplaceAFewCharactersByStarsGivenAShortEmailAddress(){
    // GIVEN
    final var email = "j.doe@lnk.ch";

    // WHEN
    final var obfuscatedEmail = obfuscator.maskEmailAddress(email);

    // THEN
    assertThat(obfuscatedEmail)
        .contains("*")
        .isNotEqualTo(email)
        .isEqualTo("j**oe@lnk.ch");
  }

  @Test
  void maskEmailAddressShouldNotReplaceGivenATinyEmailAddress(){
    // GIVEN
    final var email = "ja@gmail.com";

    // WHEN
    final var obfuscatedEmail = obfuscator.maskEmailAddress(email);

    // THEN
    assertThat(obfuscatedEmail)
        .doesNotContain("*")
        .isEqualTo(email);
  }

}
