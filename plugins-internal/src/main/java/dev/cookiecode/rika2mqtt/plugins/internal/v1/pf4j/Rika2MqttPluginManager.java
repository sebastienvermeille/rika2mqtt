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

import static org.pf4j.PluginState.*;

import com.google.common.annotations.VisibleForTesting;
import dev.cookiecode.rika2mqtt.plugins.api.v1.PluginConfiguration;
import dev.cookiecode.rika2mqtt.plugins.api.v1.Rika2MqttPlugin;
import dev.cookiecode.rika2mqtt.plugins.api.v1.annotations.ConfigurablePlugin;
import dev.cookiecode.rika2mqtt.plugins.api.v1.exceptions.InvalidPluginConfigurationException;
import java.util.*;
import lombok.NonNull;
import lombok.extern.flogger.Flogger;
import org.pf4j.*;

@Flogger
public class Rika2MqttPluginManager extends DefaultPluginManager {

  @VisibleForTesting
  static final EnumSet<PluginState> PLUGIN_STATES_PREVENTING_START = EnumSet.of(DISABLED, STARTED);

  @VisibleForTesting static final String PLUGIN_ENV_NAME_PREFIX = "PLUGIN_";

  @Override
  public void startPlugins() {
    log.atInfo().log("Start plugins");
    getResolvedPlugins().stream()
        .filter(pluginWrapper -> shouldStartPlugin(pluginWrapper.getPluginState()))
        .forEach(this::handlePlugin);
  }

  @VisibleForTesting
  boolean shouldStartPlugin(@NonNull final PluginState pluginState) {
    return !PLUGIN_STATES_PREVENTING_START.contains(pluginState);
  }

  @VisibleForTesting
  void handlePlugin(@NonNull final PluginWrapper pluginWrapper) {
    try {
      final var rika2MqttPlugin = (Rika2MqttPlugin) pluginWrapper.getPlugin();
      final var pluginConfiguration = loadPluginConfiguration(rika2MqttPlugin);

      if (isPluginConfigurationValid(rika2MqttPlugin, pluginConfiguration)) {
        startPlugin(pluginWrapper, pluginConfiguration);
      } else {
        handleInvalidPluginConfiguration(pluginWrapper);
      }
    } catch (Exception | LinkageError e) {
      handlePluginStartFailure(pluginWrapper, e);
    } finally {
      firePluginStateEvent(
          new PluginStateEvent(this, pluginWrapper, pluginWrapper.getPluginState()));
    }
  }

  @VisibleForTesting
  void handleInvalidPluginConfiguration(@NonNull final PluginWrapper pluginWrapper) {
    final var pluginName = getPluginLabel(pluginWrapper.getDescriptor());
    log.atSevere().log(
        "Plugin '%s' configuration is invalid. Aborting load of the plugin", pluginName);
    stopAndFailPlugin(pluginWrapper, pluginName);
  }

  @VisibleForTesting
  void handlePluginStartFailure(
      @NonNull final PluginWrapper pluginWrapper, @NonNull final Throwable exception) {
    pluginWrapper.setPluginState(FAILED);
    pluginWrapper.setFailedException(exception);
    log.atSevere().withCause(pluginWrapper.getFailedException()).log(
        "Unable to start plugin '%s'", getPluginLabel(pluginWrapper.getDescriptor()));
  }

  @VisibleForTesting
  void stopAndFailPlugin(
      @NonNull final PluginWrapper pluginWrapper, @NonNull final String pluginName) {
    stopPlugin(pluginWrapper.getPluginId());
    pluginWrapper.setPluginState(FAILED);
    pluginWrapper.setFailedException(
        new InvalidPluginConfigurationException(
            String.format(
                "Plugin '%s' configuration is invalid. Aborting load of the plugin", pluginName)));
  }

  @VisibleForTesting
  void startPlugin(
      @NonNull final PluginWrapper pluginWrapper,
      @NonNull final PluginConfiguration pluginConfiguration) {
    log.atInfo().log("Start plugin '%s'", getPluginLabel(pluginWrapper.getDescriptor()));
    ((Rika2MqttPlugin) pluginWrapper.getPlugin()).preStart(pluginConfiguration);
    pluginWrapper.getPlugin().start();
    pluginWrapper.setPluginState(STARTED);
    pluginWrapper.setFailedException(null);
    startedPlugins.add(pluginWrapper);
  }

  @VisibleForTesting
  boolean isPluginConfigurationValid(
      @NonNull final Rika2MqttPlugin plugin,
      @NonNull final PluginConfiguration pluginConfiguration) {

    // configurable plugins
    if (plugin instanceof ConfigurablePlugin configurablePlugin) {

      final var parameters = configurablePlugin.declarePluginConfigurationParameters();

      final List<String> errors = new ArrayList<>();

      for (final var param : parameters) {
        // check required params are provided
        if (param.isRequired()
            && pluginConfiguration.getOptionalParameter(param.getParameterName()).isEmpty()) {
          errors.add(
              String.format(
                  "Parameter '%s' is required for this plugin to work properly. However unable to find any ENV named: '%s%s' declaring any value",
                  param.getParameterName(), PLUGIN_ENV_NAME_PREFIX, param.getParameterName()));
        }
      }

      if (errors.isEmpty()) {
        return true;
      } else {
        log.atSevere().log("%s", errors);
        return false;
      }

    } else {
      return true;
    }
  }

  @VisibleForTesting
  PluginConfiguration loadPluginConfiguration(@NonNull final Rika2MqttPlugin plugin) {

    final Map<String, String> configuration = new HashMap<>();

    if (plugin instanceof ConfigurablePlugin configurablePlugin) {
      for (final var parameter : configurablePlugin.declarePluginConfigurationParameters()) {

        var value =
            getEnvironmentVariable(PLUGIN_ENV_NAME_PREFIX + parameter.getParameterName())
                .orElseGet(
                    () -> {
                      if (parameter.isOptional() && parameter.getDefaultValue().isPresent()) {
                        return parameter.getDefaultValue().get().toString();
                      } else {
                        return null;
                      }
                    });
        configuration.put(parameter.getParameterName(), value);
      }
    }

    return PluginConfiguration.builder().parameters(configuration).build();
  }

  @VisibleForTesting
  Optional<String> getEnvironmentVariable(@NonNull String environmentVariableName) {
    return Optional.ofNullable(System.getenv(environmentVariableName));
  }
}
