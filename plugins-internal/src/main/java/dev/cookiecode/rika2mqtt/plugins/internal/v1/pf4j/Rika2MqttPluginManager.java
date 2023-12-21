package dev.cookiecode.rika2mqtt.plugins.internal.v1.pf4j;

import dev.cookiecode.rika2mqtt.plugins.api.v1.PluginConfiguration;
import dev.cookiecode.rika2mqtt.plugins.api.v1.annotations.ConfigurablePlugin;
import dev.cookiecode.rika2mqtt.plugins.api.v1.exceptions.InvalidPluginConfigurationException;
import java.util.*;
import lombok.NonNull;
import lombok.extern.flogger.Flogger;
import org.pf4j.*;

@Flogger
public class Rika2MqttPluginManager extends DefaultPluginManager {
  //  @Override
  //  protected PluginFactory createPluginFactory() {
  //    return new Rika2MqttPluginFactory();
  //  }

  @Override
  public void startPlugins() {
    System.out.println("START PLUGINS");
    for (PluginWrapper pluginWrapper : resolvedPlugins) {
      PluginState pluginState = pluginWrapper.getPluginState();
      if ((PluginState.DISABLED != pluginState) && (PluginState.STARTED != pluginState)) {
        try {
          // configurable plugins
          if (pluginWrapper.getPlugin() instanceof ConfigurablePlugin configurablePlugin) {
            final var pluginName = getPluginLabel(pluginWrapper.getDescriptor());
            log.atInfo().log("Check plugin '%s' configuration", pluginName);
            final var pluginConfiguration = loadPluginConfiguration(configurablePlugin);
            if (isPluginConfigurationValid(configurablePlugin, pluginConfiguration)) {
              log.atInfo().log("Start configurable plugin '%s'", pluginName);
              //              configurablePlugin.onConfigurationLoaded(pluginConfiguration);
              ((ConfigurablePlugin) pluginWrapper.getPlugin())
                  .onConfigurationLoaded(pluginConfiguration);
              // todo: this line kills everything
              pluginWrapper.getPlugin().start();
              pluginWrapper.setPluginState(PluginState.STARTED);
              pluginWrapper.setFailedException(null);
              startedPlugins.add(pluginWrapper);
            } else {
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
            log.atInfo().log("Start plugin '%s'", getPluginLabel(pluginWrapper.getDescriptor()));
            pluginWrapper.getPlugin().start();
            pluginWrapper.setPluginState(PluginState.STARTED);
            pluginWrapper.setFailedException(null);
            startedPlugins.add(pluginWrapper);
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

  private PluginConfiguration loadPluginConfiguration(ConfigurablePlugin configurablePlugin) {

    Map<String, String> configuration = new HashMap<>();

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

    return PluginConfiguration.builder().parameters(configuration).build();
  }

  private Optional<String> getEnvironmentVariable(@NonNull String environmentVariableName) {
    return Optional.ofNullable(System.getenv(environmentVariableName));
  }
}
