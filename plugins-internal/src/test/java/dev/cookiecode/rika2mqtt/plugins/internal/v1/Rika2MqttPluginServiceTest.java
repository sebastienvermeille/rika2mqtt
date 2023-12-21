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

import static org.mockito.Mockito.*;

import dev.cookiecode.rika2mqtt.plugins.api.v1.StoveStatusExtension;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.StoveStatus;
import dev.cookiecode.rika2mqtt.plugins.internal.v1.event.PolledStoveStatusEvent;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pf4j.PluginManager;

/** Test class */
@ExtendWith(MockitoExtension.class)
class Rika2MqttPluginServiceTest {

  @InjectMocks private Rika2MqttPluginService rika2MqttPluginService;

  @Mock private PluginManager pluginManager;

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
}
