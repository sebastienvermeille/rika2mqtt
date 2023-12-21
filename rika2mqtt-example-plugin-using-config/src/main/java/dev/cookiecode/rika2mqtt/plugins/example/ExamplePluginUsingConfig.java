package dev.cookiecode.rika2mqtt.plugins.example;

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

import dev.cookiecode.rika2mqtt.plugins.api.v1.Rika2MqttPlugin;
import dev.cookiecode.rika2mqtt.plugins.api.v1.annotations.ConfigurablePlugin;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.plugins.OptionalPluginConfigurationParameter;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.plugins.PluginConfigurationParameter;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.plugins.RequiredPluginConfigurationParameter;
import java.util.List;
import lombok.extern.flogger.Flogger;

@Flogger
public class ExamplePluginUsingConfig extends Rika2MqttPlugin implements ConfigurablePlugin {

  static final String PLUGIN_NAME = "ExamplePlugin";

  //  protected ExamplePluginUsingConfig(PluginContext pluginContext) {
  //    super(pluginContext);
  //  }

  @Override
  public void start() {
    log.atInfo().log("%s >> STARTED", PLUGIN_NAME);
  }

  @Override
  public void stop() {
    log.atInfo().log("%s >> STOPPED", PLUGIN_NAME);
  }

  @Override
  public List<PluginConfigurationParameter> declarePluginConfigurationParameters() {
    return List.of(
        // Optional Parameters

        // exhaustiveDeclaration
        OptionalPluginConfigurationParameter.builder()
            .withParameterName("example.plugin.postgres.port")
            .withDescription("Postgres server port")
            .withValueType(Integer.class)
            .withDefaultValue(5432)
            .withExample("5432")
            .build(),

        // short declaration (no default, no example)
        OptionalPluginConfigurationParameter.builder()
            .withParameterName("example.plugin.influxdb.auth-token")
            .withDescription("Influxdb authentication token")
            .withValueType(String.class)
            .build(),

        // Required parameters

        // exhaustive declaration
        RequiredPluginConfigurationParameter.builder()
            .withParameterName("example.plugin.postgres.host")
            .withDescription("Postgres server hostname or IP")
            .withValueType(String.class)
            .withExample("hostname, 127.0.0.1, 0.0.0.0")
            .build(),

        // short declaration (no example)
        RequiredPluginConfigurationParameter.builder()
            .withParameterName("example.plugin.postgres.dbname")
            .withDescription("Postgres database name")
            .withValueType(String.class)
            .build());
  }
}
