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
package dev.cookiecode.rika2mqtt.plugins.internal.v1;

import static dev.cookiecode.rika2mqtt.plugins.internal.v1.Rika2MqttPluginService.DEFAULT_PLUGINS_DIR;
import static dev.cookiecode.rika2mqtt.plugins.internal.v1.Rika2MqttPluginService.PLUGINS_DIR_ENV_VAR_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import dev.cookiecode.rika2mqtt.plugins.api.v1.StoveErrorExtension;
import dev.cookiecode.rika2mqtt.plugins.api.v1.StoveStatusExtension;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.StoveError;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.StoveStatus;
import dev.cookiecode.rika2mqtt.plugins.internal.v1.event.PolledStoveStatusEvent;
import dev.cookiecode.rika2mqtt.plugins.internal.v1.event.StoveErrorEvent;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pf4j.PluginManager;
import org.springframework.core.env.Environment;

/**
 * Test class
 *
 * @author Sebastien Vermeille
 */
@ExtendWith(MockitoExtension.class)
class Rika2MqttPluginServiceTest {

  @InjectMocks private Rika2MqttPluginService rika2MqttPluginService;

  @Mock private Environment environment;
  @Mock private PluginManager pluginManager;
  @Mock private PluginSyncManager pluginSyncManager;
  @Mock private PluginUrlsProvider pluginUrlsProvider;

  @Test
  void startShouldInvokeSynchronizePlugins() {

    // GIVEN
    // nothing particular

    // WHEN
    rika2MqttPluginService.start();

    // THEN
    verify(pluginSyncManager, times(1)).synchronize(anyString(), anyList());
  }

  @Test
  void startShouldInvokeLoadPlugins() {

    // GIVEN
    // nothing particular

    // WHEN
    rika2MqttPluginService.start();

    // THEN
    verify(pluginManager, times(1)).loadPlugins();
  }

  @Test
  void startShouldInvokeStartPlugins() {

    // GIVEN
    // nothing particular

    // WHEN
    rika2MqttPluginService.start();

    // THEN
    verify(pluginManager, times(1)).startPlugins();
  }

  @Test
  void
      handlePolledStoveStatusEventShouldPropagateTheEventToAllRegisteredExtensionsGivenThereAreTwo() {

    // GIVEN
    final var event = mock(PolledStoveStatusEvent.class);
    final var stoveStatus = mock(StoveStatus.class);
    when(event.getStoveStatus()).thenReturn(stoveStatus);

    // two plugins extensions
    final var extensionAlpha = mock(StoveStatusExtension.class);
    final var extensionBeta = mock(StoveStatusExtension.class);
    final var extensions = List.of(extensionAlpha, extensionBeta);
    when(pluginManager.getExtensions(StoveStatusExtension.class)).thenReturn(extensions);

    // WHEN
    rika2MqttPluginService.handlePolledStoveStatusEvent(event);

    // THEN
    verify(extensionAlpha, times(1)).onPollStoveStatusSucceed(stoveStatus);
    verify(extensionBeta, times(1)).onPollStoveStatusSucceed(stoveStatus);
  }

  @Test
  void handleStoveErrorEventShouldPropagateTheEventToAllRegisteredExtensionsGivenThereAreTwo() {

    // GIVEN
    final var event = mock(StoveErrorEvent.class);
    final var stoveError = mock(StoveError.class);
    when(event.getStoveError()).thenReturn(stoveError);

    // two plugins extensions
    final var extensionAlpha = mock(StoveErrorExtension.class);
    final var extensionBeta = mock(StoveErrorExtension.class);
    final var extensions = List.of(extensionAlpha, extensionBeta);
    when(pluginManager.getExtensions(StoveErrorExtension.class)).thenReturn(extensions);

    // WHEN
    rika2MqttPluginService.handleStoveErrorEvent(event);

    // THEN
    verify(extensionAlpha, times(1)).onStoveError(stoveError);
    verify(extensionBeta, times(1)).onStoveError(stoveError);
  }

  @Test
  void getPluginsDirShouldReturnDefaultPluginsDirGivenEnvIsNotSet() {
    // GIVEN
    doReturn(null).when(environment).getProperty(PLUGINS_DIR_ENV_VAR_NAME);

    // WHEN
    final var pluginsDir = rika2MqttPluginService.getPluginsDir();

    // THEN
    assertThat(pluginsDir).isEqualTo(DEFAULT_PLUGINS_DIR);
  }

  @Test
  void getPluginsDirShouldReturnPluginsDirProvidedValueDirGivenEnvIsSet() {
    // GIVEN
    final var somedir = "somedir";
    doReturn(somedir).when(environment).getProperty(PLUGINS_DIR_ENV_VAR_NAME);

    // WHEN
    final var pluginsDir = rika2MqttPluginService.getPluginsDir();

    // THEN
    assertThat(pluginsDir).isEqualTo(somedir);
  }
}
