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
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test class
 *
 * @author Sebastien Vermeille
 */
@ExtendWith(MockitoExtension.class)
class Rika2MqttPluginTest {

  @Test
  void preStartShouldInitPluginConfigurationAsItIsTheWholePurposeOfIt() {
    // GIVEN
    final var plugin = new DummyPlugin();
    final var pluginConfiguration = mock(PluginConfiguration.class);

    // WHEN
    plugin.preStart(pluginConfiguration);

    // THEN
    assertThat(plugin.getPluginConfiguration()).isEqualTo(pluginConfiguration);
  }

  @Test
  void
      getPluginConfigurationParameterShouldInvokePluginConfigurationGetParameterMethodGivenItsAConvenienceWrapper() {
    // GIVEN
    final var plugin = new DummyPlugin();
    final var pluginConfiguration = mock(PluginConfiguration.class);

    plugin.preStart(pluginConfiguration);

    final var parameterName = "something";

    // WHEN
    plugin.getPluginConfigurationParameter(parameterName);

    // THEN
    verify(pluginConfiguration, times(1)).getParameter(parameterName);
  }
}
