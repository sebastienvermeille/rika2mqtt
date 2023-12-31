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
package dev.cookiecode.rika2mqtt.plugins.internal.v1.pf4j;

import static dev.cookiecode.rika2mqtt.plugins.internal.v1.pf4j.Rika2MqttPluginManager.PLUGIN_ENV_NAME_PREFIX;
import static dev.cookiecode.rika2mqtt.plugins.internal.v1.pf4j.Rika2MqttPluginManager.PLUGIN_STATES_PREVENTING_START;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;
import static org.pf4j.PluginState.*;

import dev.cookiecode.rika2mqtt.plugins.api.v1.PluginConfiguration;
import dev.cookiecode.rika2mqtt.plugins.api.v1.exceptions.InvalidPluginConfigurationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pf4j.PluginDescriptor;
import org.pf4j.PluginWrapper;

/**
 * Test class
 *
 * @author Sebastien Vermeille
 */
@ExtendWith(MockitoExtension.class)
class Rika2MqttPluginManagerTest {

  private final Map<String, String> mockedAnswers = new HashMap<>();

  @InjectMocks @Spy private Rika2MqttPluginManager rika2MqttPluginManager;

  @BeforeEach
  void beforeEach() {
    mockedAnswers.clear();
  }

  @Test
  void startPluginsShouldInvokeHandlePluginsMethodOnlyToOnePluginGivenOnePluginIsDisabled() {

    // GIVEN
    final var createdPlugin = mock(PluginWrapper.class);
    when(createdPlugin.getPluginState()).thenReturn(CREATED);
    final var disabledPlugin = mock(PluginWrapper.class);
    when(disabledPlugin.getPluginState()).thenReturn(DISABLED);

    doReturn(List.of(createdPlugin, disabledPlugin))
        .when(rika2MqttPluginManager)
        .getResolvedPlugins();
    doNothing().when(rika2MqttPluginManager).handlePlugin(createdPlugin);

    // WHEN
    rika2MqttPluginManager.startPlugins();

    // THEN
    verify(rika2MqttPluginManager, times(1)).handlePlugin(createdPlugin);
    verify(rika2MqttPluginManager, never()).handlePlugin(disabledPlugin);
  }

  @Test
  void shouldStartPluginShouldReturnFalseGivenPluginIsInDisabledState() {

    // GIVEN
    final var state = DISABLED;

    // WHEN
    final var shouldStart = rika2MqttPluginManager.shouldStartPlugin(state);

    // THEN
    assertThat(shouldStart).isFalse();
  }

  @Test
  void shouldStartPluginShouldReturnFalseGivenPluginIsInStartedState() {

    // GIVEN
    final var state = STARTED;

    // WHEN
    final var shouldStart = rika2MqttPluginManager.shouldStartPlugin(state);

    // THEN
    assertThat(shouldStart).isFalse();
  }

  @Test
  void shouldStartPluginShouldReturnTrueGivenPluginIsInStoppedState() {

    // GIVEN
    final var state = STOPPED;

    // WHEN
    final var shouldStart = rika2MqttPluginManager.shouldStartPlugin(state);

    // THEN
    assertThat(shouldStart).isTrue();
  }

  @Test
  void shouldStartPluginShouldReturnTrueGivenPluginIsInCreatedState() {

    // GIVEN
    final var state = CREATED;

    // WHEN
    final var shouldStart = rika2MqttPluginManager.shouldStartPlugin(state);

    // THEN
    assertThat(shouldStart).isTrue();
  }

  @Test
  void shouldStartPluginShouldReturnFalseForAllEnumeratedPluginStates() {
    for (final var state : PLUGIN_STATES_PREVENTING_START) {
      // GIVEN
      // a state which is supposed to prevent the plugin to start

      // WHEN
      final var shouldStart = rika2MqttPluginManager.shouldStartPlugin(state);

      // THEN
      assertThat(shouldStart).isFalse();
    }
  }

  @Test
  void
      loadPluginConfigurationShouldReturnAPluginConfigurationWithEmptyParametersGivenThePluginIsNotAConfigurablePlugin() {
    // GIVEN
    final var notConfigurablePlugin = new NotConfigurablePlugin();

    // WHEN
    final var pluginConfiguration =
        rika2MqttPluginManager.loadPluginConfiguration(notConfigurablePlugin);

    // THEN
    assertThat(pluginConfiguration.getParameters()).isEmpty();
  }

  @Test
  void
      loadPluginConfigurationShouldReturnAPluginConfigurationIncludingParametersGivenThePluginIsAConfigurablePlugin() {
    // GIVEN
    final var configurablePlugin = new DummyConfigurablePlugin();

    // WHEN
    final var pluginConfiguration =
        rika2MqttPluginManager.loadPluginConfiguration(configurablePlugin);

    // THEN
    assertThat(pluginConfiguration.getParameters()).isNotEmpty();
  }

  @Test
  void
      loadPluginConfigurationShouldReturnAPluginConfigurationIncludingParametersGivenThePluginDeclareAnOptionalParameterWithDefaultValueAndNoEnvIsGiven() {
    // GIVEN
    final var configurablePlugin = new DummyConfigurablePlugin();

    // WHEN
    final var pluginConfiguration =
        rika2MqttPluginManager.loadPluginConfiguration(configurablePlugin);

    // THEN
    assertThat(pluginConfiguration.getOptionalParameter("language")).isEqualTo(Optional.of("en"));
  }

  @Test
  void
      loadPluginConfigurationShouldReturnAPluginConfigurationIncludingParametersGivenThePluginDeclareAnOptionalParameterWithDefaultValueAndEnvIsGiven() {
    // GIVEN
    final var configurablePlugin = new DummyConfigurablePlugin();
    final var language = "fr";
    mockPluginEnv("language", language);

    // WHEN
    final var pluginConfiguration =
        rika2MqttPluginManager.loadPluginConfiguration(configurablePlugin);

    // THEN
    assertThat(pluginConfiguration.getOptionalParameter("language"))
        .isEqualTo(Optional.of(language));
  }

  @Test
  void
      loadPluginConfigurationShouldReturnAPluginConfigurationIncludingParametersGivenThePluginDeclareARequiredParameterAndNoEnvIsProvided() {
    // GIVEN
    final var configurablePlugin = new DummyConfigurablePlugin();

    // WHEN
    final var pluginConfiguration =
        rika2MqttPluginManager.loadPluginConfiguration(configurablePlugin);

    // THEN
    assertThat(pluginConfiguration.getOptionalParameter("password"))
        .as(
            "The password parameter is not defined in ENV so it is set to null and then rika2mqtt will perform a validation of the plugin configuration")
        .isEmpty();
  }

  @Test
  void
      loadPluginConfigurationShouldReturnAPluginConfigurationIncludingParametersGivenThePluginDeclareARequiredParameterAndEnvIsProvided() {
    // GIVEN
    final var configurablePlugin = new DummyConfigurablePlugin();
    final var password = "p4ssw0rd";
    mockPluginEnv("password", password);

    // WHEN
    final var pluginConfiguration =
        rika2MqttPluginManager.loadPluginConfiguration(configurablePlugin);

    // THEN
    assertThat(pluginConfiguration.getParameter("password")).isEqualTo(password);
  }

  @Test
  void startPluginShouldInvokePluginPreStartMethod() {

    // GIVEN
    final var pluginWrapper = mock(PluginWrapper.class);
    when(pluginWrapper.getDescriptor()).thenReturn(mock(PluginDescriptor.class));
    final var rikaPlugin = spy(new DummyConfigurablePlugin());
    when(pluginWrapper.getPlugin()).thenReturn(rikaPlugin);
    final var pluginConfiguration = mock(PluginConfiguration.class);

    // WHEN
    rika2MqttPluginManager.startPlugin(pluginWrapper, pluginConfiguration);

    // THEN
    verify(rikaPlugin, times(1)).preStart(pluginConfiguration);
  }

  @Test
  void startPluginShouldInvokePluginStartMethod() {

    // GIVEN
    final var pluginWrapper = mock(PluginWrapper.class);
    when(pluginWrapper.getDescriptor()).thenReturn(mock(PluginDescriptor.class));
    final var rikaPlugin = spy(new DummyConfigurablePlugin());
    when(pluginWrapper.getPlugin()).thenReturn(rikaPlugin);
    final var pluginConfiguration = mock(PluginConfiguration.class);

    // WHEN
    rika2MqttPluginManager.startPlugin(pluginWrapper, pluginConfiguration);

    // THEN
    verify(rikaPlugin, times(1)).start();
  }

  @Test
  void startPluginShouldUpdatePluginStateWhenInvoked() {

    // GIVEN
    final var pluginWrapper = mock(PluginWrapper.class);
    when(pluginWrapper.getDescriptor()).thenReturn(mock(PluginDescriptor.class));
    final var rikaPlugin = spy(new DummyConfigurablePlugin());
    when(pluginWrapper.getPlugin()).thenReturn(rikaPlugin);
    final var pluginConfiguration = mock(PluginConfiguration.class);

    // WHEN
    rika2MqttPluginManager.startPlugin(pluginWrapper, pluginConfiguration);

    // THEN
    verify(pluginWrapper, times(1)).setPluginState(STARTED);
    verify(pluginWrapper, times(1)).setFailedException(null);
  }

  @Test
  void handleInvalidPluginConfigurationShouldInvokeStopAndFailPlugin() {

    // GIVEN
    final var pluginWrapper = mock(PluginWrapper.class);
    final var pluginDescriptor = mock(PluginDescriptor.class);
    doReturn(pluginDescriptor).when(pluginWrapper).getDescriptor();
    doNothing().when(rika2MqttPluginManager).stopAndFailPlugin(any(), anyString());

    // WHEN
    rika2MqttPluginManager.handleInvalidPluginConfiguration(pluginWrapper);

    // THEN
    verify(rika2MqttPluginManager, times(1)).stopAndFailPlugin(any(), anyString());
  }

  @Test
  void handlePluginStartFailureShouldUpdatePluginState() {

    // GIVEN
    final var pluginWrapper = mock(PluginWrapper.class);
    final var pluginDescriptor = mock(PluginDescriptor.class);
    doReturn(pluginDescriptor).when(pluginWrapper).getDescriptor();
    final var exception = mock(Exception.class);

    // WHEN
    rika2MqttPluginManager.handlePluginStartFailure(pluginWrapper, exception);

    // THEN
    verify(pluginWrapper, times(1)).setPluginState(FAILED);
    verify(pluginWrapper, times(1)).setFailedException(exception);
  }

  @Test
  void stopAndFailPluginShouldUpdatePluginStateAccordingly() {

    // GIVEN
    final var pluginWrapper = mock(PluginWrapper.class);
    final var pluginId = "plugin-id";
    when(pluginWrapper.getPluginId()).thenReturn(pluginId);
    doReturn(null).when(rika2MqttPluginManager).stopPlugin(anyString());
    final var pluginName = "plugin-a";

    // WHEN
    rika2MqttPluginManager.stopAndFailPlugin(pluginWrapper, pluginName);

    // THEN
    verify(pluginWrapper, times(1)).setPluginState(FAILED);
    verify(pluginWrapper, times(1))
        .setFailedException(any(InvalidPluginConfigurationException.class));
  }

  @Test
  void handlePluginShouldInvokeStartPluginGivenTheConfigurationIsValid() {

    // GIVEN
    final var pluginWrapper = mock(PluginWrapper.class);
    final var rika2mqttPlugin = new DummyConfigurablePlugin();
    when(pluginWrapper.getPlugin()).thenReturn(rika2mqttPlugin);
    final var pluginDescriptor = mock(PluginDescriptor.class);
    doReturn(pluginDescriptor).when(pluginWrapper).getDescriptor();
    doReturn(true).when(rika2MqttPluginManager).isPluginConfigurationValid(any(), any());

    // WHEN
    rika2MqttPluginManager.handlePlugin(pluginWrapper);

    // THEN
    verify(rika2MqttPluginManager, times(1)).startPlugin(any(), any());
  }

  @Test
  void handlePluginShouldInvokeHandleInvalidPluginConfigurationGivenTheConfigurationIsInvalid() {

    // GIVEN
    final var pluginWrapper = mock(PluginWrapper.class);
    final var rika2mqttPlugin = new DummyConfigurablePlugin();
    when(pluginWrapper.getPlugin()).thenReturn(rika2mqttPlugin);
    final var pluginDescriptor = mock(PluginDescriptor.class);
    doReturn(pluginDescriptor).when(pluginWrapper).getDescriptor();
    doReturn(false).when(rika2MqttPluginManager).isPluginConfigurationValid(any(), any());

    // WHEN
    rika2MqttPluginManager.handlePlugin(pluginWrapper);

    // THEN
    verify(rika2MqttPluginManager, times(1)).handleInvalidPluginConfiguration(any());
  }

  @Test
  void
      isPluginConfigurationValidShouldReturnTrueGivenThePluginIsNotImplementingConfigurablePlugin() {

    // GIVEN
    final var plugin = new NotConfigurablePlugin();
    final var pluginConfiguration = mock(PluginConfiguration.class);

    // WHEN
    final var valid =
        rika2MqttPluginManager.isPluginConfigurationValid(plugin, pluginConfiguration);

    // THEN
    assertThat(valid).isTrue();
  }

  @Test
  void
      isPluginConfigurationValidShouldReturnFalseGivenTheConfigurablePluginDeclaresARequiredParameterAndItIsNotProvided() {

    // GIVEN
    // intentionally do not define a password ENV

    final var plugin = new DummyConfigurablePlugin();
    when(rika2MqttPluginManager.loadPluginConfiguration(plugin)).thenCallRealMethod();
    final var pluginConfiguration = rika2MqttPluginManager.loadPluginConfiguration(plugin);

    // WHEN
    final var valid =
        rika2MqttPluginManager.isPluginConfigurationValid(plugin, pluginConfiguration);

    // THEN
    assertThat(valid).isFalse();
  }

  @Test
  void
      isPluginConfigurationValidShouldReturnTrueGivenTheConfigurablePluginDeclaresARequiredParameterAndIsProvided() {

    // GIVEN
    mockPluginEnv("password", "p4ssw0rd");

    final var plugin = new DummyConfigurablePlugin();
    when(rika2MqttPluginManager.loadPluginConfiguration(plugin)).thenCallRealMethod();
    final var pluginConfiguration = rika2MqttPluginManager.loadPluginConfiguration(plugin);

    // WHEN
    final var valid =
        rika2MqttPluginManager.isPluginConfigurationValid(plugin, pluginConfiguration);

    // THEN
    assertThat(valid).isTrue();
  }

  /** Helper method for testing plugin configurations */
  private void mockPluginEnv(
      @NonNull final String pluginParameterName, @NonNull final String value) {
    mockedAnswers.put(pluginParameterName, value);
    doAnswer(
            invocation -> {
              final var arg = (String) invocation.getArgument(0);
              final var keyName = arg.replaceFirst(PLUGIN_ENV_NAME_PREFIX, "");
              return Optional.ofNullable(mockedAnswers.get(keyName));
            })
        .when(rika2MqttPluginManager)
        .getEnvironmentVariable(anyString());
  }

  @Test
  void shouldStartPluginShouldThrowAnNullPointerExceptionGivenNoParameterIsPassed() {
    assertThrows(
        NullPointerException.class,
        () -> {
          // WHEN
          rika2MqttPluginManager.shouldStartPlugin(null);
        });
  }

  @Test
  void handlePluginShouldThrowAnNullPointerExceptionGivenNoParameterIsPassed() {
    assertThrows(
        NullPointerException.class,
        () -> {
          // WHEN
          rika2MqttPluginManager.handlePlugin(null);
        });
  }

  @Test
  void handleInvalidPluginConfigurationShouldThrowAnNullPointerExceptionGivenNoParameterIsPassed() {
    assertThrows(
        NullPointerException.class,
        () -> {
          // WHEN
          rika2MqttPluginManager.handleInvalidPluginConfiguration(null);
        });
  }

  @Test
  void handlePluginStartFailureShouldThrowAnNullPointerExceptionGivenNoParameterIsPassed() {
    assertThrows(
        NullPointerException.class,
        () -> {
          // WHEN
          rika2MqttPluginManager.handlePluginStartFailure(null, null);
        });
  }

  @Test
  void stopAndFailPluginShouldThrowAnNullPointerExceptionGivenNoParameterIsPassed() {
    assertThrows(
        NullPointerException.class,
        () -> {
          // WHEN
          rika2MqttPluginManager.stopAndFailPlugin(null, null);
        });
  }

  @Test
  void isPluginConfigurationValidShouldThrowAnNullPointerExceptionGivenNoParameterIsPassed() {
    assertThrows(
        NullPointerException.class,
        () -> {
          // WHEN
          rika2MqttPluginManager.isPluginConfigurationValid(null, null);
        });
  }

  @Test
  void loadPluginConfigurationShouldThrowAnNullPointerExceptionGivenNoParameterIsPassed() {
    assertThrows(
        NullPointerException.class,
        () -> {
          // WHEN
          rika2MqttPluginManager.loadPluginConfiguration(null);
        });
  }

  @Test
  void getEnvironmentVariableShouldThrowAnNullPointerExceptionGivenNoParameterIsPassed() {
    assertThrows(
        NullPointerException.class,
        () -> {
          // WHEN
          rika2MqttPluginManager.getEnvironmentVariable(null);
        });
  }
}
