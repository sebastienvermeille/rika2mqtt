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
  void setUp() {
    obfuscator = new EmailObfuscator();
  }

  @Test
  void maskEmailAddressShouldReplaceAFewCharactersByStarsGivenAnEmailAddress() {
    // GIVEN
    final var email = "jean.marc@gmail.com";

    // WHEN
    final var obfuscatedEmail = obfuscator.maskEmailAddress(email);

    // THEN
    assertThat(obfuscatedEmail).contains("*").isNotEqualTo(email).isEqualTo("je****arc@gmail.com");
  }

  @Test
  void maskEmailAddressShouldReplaceAFewCharactersByStarsGivenAShortEmailAddress() {
    // GIVEN
    final var email = "j.doe@lnk.ch";

    // WHEN
    final var obfuscatedEmail = obfuscator.maskEmailAddress(email);

    // THEN
    assertThat(obfuscatedEmail).contains("*").isNotEqualTo(email).isEqualTo("j**oe@lnk.ch");
  }

  @Test
  void maskEmailAddressShouldNotReplaceGivenATinyEmailAddress() {
    // GIVEN
    final var email = "ja@gmail.com";

    // WHEN
    final var obfuscatedEmail = obfuscator.maskEmailAddress(email);

    // THEN
    assertThat(obfuscatedEmail).doesNotContain("*").isEqualTo(email);
  }
}
