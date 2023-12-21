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

  @Override
  public void startPlugins() {
    log.atInfo().log("Start plugins");
    for (PluginWrapper pluginWrapper : resolvedPlugins) {
      PluginState pluginState = pluginWrapper.getPluginState();
      if ((PluginState.DISABLED != pluginState) && (PluginState.STARTED != pluginState)) {
        try {

          final var pluginConfiguration =
              loadPluginConfiguration((Rika2MqttPlugin) pluginWrapper.getPlugin());

          // configurable plugins
          if (pluginWrapper.getPlugin() instanceof ConfigurablePlugin configurablePlugin) {
            if (isPluginConfigurationValid(configurablePlugin, pluginConfiguration)) {
              startPlugin(pluginWrapper, pluginConfiguration);
            } else {
              final var pluginName = getPluginLabel(pluginWrapper.getDescriptor());
              log.atSevere().log(
                  "Plugin '%s' configuration is invalid. Aborting load of the plugin", pluginName);
              stopPlugin(pluginWrapper.getPluginId());
              pluginWrapper.setPluginState(PluginState.FAILED);
              pluginWrapper.setFailedException(
                  new InvalidPluginConfigurationException(
                      String.format(
                          "Plugin '%s' configuration is invalid. Aborting load of the plugin",
                          pluginName)));
            }
          } else {
            startPlugin(pluginWrapper, pluginConfiguration);
          }
        } catch (Exception | LinkageError e) {
          pluginWrapper.setPluginState(PluginState.FAILED);
          pluginWrapper.setFailedException(e);
          log.atSevere().withCause(e).log(
              "Unable to start plugin '%s'", getPluginLabel(pluginWrapper.getDescriptor()));
        } finally {
          firePluginStateEvent(new PluginStateEvent(this, pluginWrapper, pluginState));
        }
      }
    }
  }

  private void startPlugin(PluginWrapper pluginWrapper, PluginConfiguration pluginConfiguration){
    log.atInfo().log("Start plugin '%s'", getPluginLabel(pluginWrapper.getDescriptor()));
    ((Rika2MqttPlugin) pluginWrapper.getPlugin()).preStart(pluginConfiguration);
    pluginWrapper.getPlugin().start();
    pluginWrapper.setPluginState(PluginState.STARTED);
    pluginWrapper.setFailedException(null);
    startedPlugins.add(pluginWrapper);
  }

  private boolean isPluginConfigurationValid(
      @NonNull ConfigurablePlugin configurablePlugin,
      @NonNull PluginConfiguration pluginConfiguration) {

    final var parameters = configurablePlugin.declarePluginConfigurationParameters();

    final List<String> errors = new ArrayList<>();

    for (final var param : parameters) {
      // check required params are provided
      if (param.isRequired()
          && pluginConfiguration.getParameter(param.getParameterName()).isEmpty()) {
        errors.add(
            String.format(
                "Parameter '%s' is required for this plugin to work properly. However unable to find any ENV named: 'PLUGIN_%s' declaring any value",
                param.getParameterName(), param.getParameterName()));
      }
    }

    if (errors.isEmpty()) {
      return true;
    } else {
      log.atSevere().log("%s", errors);
      return false;
    }
  }

  private PluginConfiguration loadPluginConfiguration(@NonNull Rika2MqttPlugin plugin) {

    Map<String, String> configuration = new HashMap<>();

    if (plugin instanceof ConfigurablePlugin configurablePlugin) {
      for (var parameter : configurablePlugin.declarePluginConfigurationParameters()) {

        var value =
            getEnvironmentVariable("PLUGIN_" + parameter.getParameterName())
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

  private Optional<String> getEnvironmentVariable(@NonNull String environmentVariableName) {
    return Optional.ofNullable(System.getenv(environmentVariableName));
  }
}
