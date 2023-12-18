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
class Rika2MqttPluginManagerTest {

  @InjectMocks private Rika2MqttPluginManager rika2MqttPluginManager;

  @Mock private PluginManager pluginManager;

  @Test
  void startShouldInvokeLoadPlugins() {

    // GIVEN
    // nothing particular

    // WHEN
    rika2MqttPluginManager.start();

    // THEN
    verify(pluginManager, times(1)).loadPlugins();
  }

  @Test
  void startShouldInvokeStartPlugins() {

    // GIVEN
    // nothing particular

    // WHEN
    rika2MqttPluginManager.start();

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
    rika2MqttPluginManager.handlePolledStoveStatusEvent(event);

    // THEN
    verify(extensionAlpha, times(1)).onPollStoveStatusSucceed(stoveStatus);
    verify(extensionBeta, times(1)).onPollStoveStatusSucceed(stoveStatus);
  }
}
