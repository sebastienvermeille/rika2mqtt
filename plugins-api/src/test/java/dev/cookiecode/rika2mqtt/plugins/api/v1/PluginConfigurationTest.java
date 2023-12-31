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
package dev.cookiecode.rika2mqtt.plugins.api.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test class
 *
 * @author Sebastien Vermeille
 */
@ExtendWith(MockitoExtension.class)
class PluginConfigurationTest {

  @InjectMocks @Spy private PluginConfiguration pluginConfiguration;

  @Test
  void getParameterShouldInvokeGetOptionalParameterGivenThatThisIsMoreErrorSafe() {
    // GIVEN
    final var parameterName = "something";
    doReturn(Optional.of("some value")).when(pluginConfiguration).getOptionalParameter(anyString());

    // WHEN
    pluginConfiguration.getParameter(parameterName);

    // THEN
    verify(pluginConfiguration, times(1)).getOptionalParameter(parameterName);
  }

  @Test
  void getParameterShouldThrowAnExceptionGivenTheOptionalIsEmpty() {
    // GIVEN
    final var parameterName = "something";
    doReturn(Optional.empty()).when(pluginConfiguration).getOptionalParameter(anyString());

    // THEN
    assertThrows(
        NoSuchElementException.class,
        () -> {
          // WHEN
          pluginConfiguration.getParameter(parameterName);
        });
  }

  @Test
  void getOptionalParameterShouldReturnAnEmptyOptionalGivenTheRequestedParameterIsNotProvided() {
    // GIVEN
    final var parameterName = "somethingThatDoNotExists";
    final var emptyPluginConfiguration =
        PluginConfiguration.builder().parameters(new HashMap<>()).build();

    // WHEN
    final var result = emptyPluginConfiguration.getOptionalParameter(parameterName);

    // THEN
    assertThat(result).isEmpty();
  }

  @Test
  void getOptionalParameterShouldReturnAValueGivenTheRequestedParameterIsProvidedWithinTheConfig() {
    // GIVEN
    final var parameterName = "username";
    final var parameterValue = "root";
    final var filledPluginConfiguration =
        PluginConfiguration.builder().parameters(Map.of(parameterName, parameterValue)).build();

    // WHEN
    final var result = filledPluginConfiguration.getOptionalParameter(parameterName);

    // THEN
    assertThat(result).isPresent().isEqualTo(Optional.of(parameterValue));
  }

  @Test
  void getOptionalParameterShouldThrowAnNullPointerExceptionGivenNoParameterIsPassed() {
    assertThrows(
        NullPointerException.class,
        () -> {
          // WHEN
          pluginConfiguration.getOptionalParameter(null);
        });
  }

  @Test
  void getParameterShouldThrowAnNullPointerExceptionGivenNoParameterIsPassed() {
    assertThrows(
        NullPointerException.class,
        () -> {
          // WHEN
          pluginConfiguration.getParameter(null);
        });
  }
}
