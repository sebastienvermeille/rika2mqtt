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
package dev.cookiecode.rika2mqtt.plugins.influxdb.metrics;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import dev.cookiecode.rika2mqtt.plugins.api.v1.PluginConfiguration;
import dev.cookiecode.rika2mqtt.plugins.api.v1.Rika2MqttPlugin;
import dev.cookiecode.rika2mqtt.plugins.api.v1.annotations.ConfigurablePlugin;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.plugins.OptionalPluginConfigurationParameter;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.plugins.PluginConfigurationParameter;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.plugins.RequiredPluginConfigurationParameter;
import java.util.HashMap;
import java.util.List;
import kamon.Kamon;

/** A plugin to export rika2mqtt metrics to InfluxDB */
public class Rika2MqttInfluxMetricsPlugin extends Rika2MqttPlugin implements ConfigurablePlugin {
  private static final String INFLUXDB_HOSTNAME = "INFLUXDB_HOSTNAME";
  private static final String INFLUXDB_PORT = "INFLUXDB_PORT";
  private static final String INFLUXDB_DATABASE = "INFLUXDB_DATABASE";
  private static final String INFLUXDB_PROTOCOL = "INFLUXDB_PROTOCOL";
  private static final String INFLUXDB_AUTHENTICATION_TOKEN = "INFLUXDB_AUTHENTICATION_TOKEN";

  private PluginConfiguration pluginConfiguration;

  private StoveStatusHook hook;

  @Override
  public void start() {
    Kamon.init();
    Kamon.reconfigure(loadConfig());
    Kamon.loadModules();

    hook = new StoveStatusHook(); // need a field otherwise it doesnt work (TODO: check why)
  }

  @Override
  public void stop() {
    log.atInfo().log("Stopping Kamon...");
    Kamon.stop();
  }

  Config loadConfig() {
    var defaultConfig = Kamon.config();

    // TODO: have to be customizable
    var props = new HashMap<String, Object>();
    props.put("kamon.influxdb.port", pluginConfiguration.getParameter(INFLUXDB_PORT));
    props.put("kamon.influxdb.hostname", pluginConfiguration.getParameter(INFLUXDB_HOSTNAME));
    props.put("kamon.influxdb.database", pluginConfiguration.getParameter(INFLUXDB_DATABASE));
    props.put("kamon.influxdb.protocol", pluginConfiguration.getParameter(INFLUXDB_PROTOCOL));
    props.put(
        "kamon.influxdb.authentication.token",
        pluginConfiguration.getParameter(INFLUXDB_AUTHENTICATION_TOKEN));

    var codeConfig = ConfigFactory.parseMap(props);
    var newConfig = codeConfig.withFallback(defaultConfig);
    return newConfig;
  }

  @Override
  public List<PluginConfigurationParameter> declarePluginConfigurationParameters() {
    return List.of(
        RequiredPluginConfigurationParameter.builder()
            .withParameterName(INFLUXDB_HOSTNAME)
            .withDescription("Hostname or IP of the influxdb server")
            .withValueType(String.class)
            .withExample("localhost, 127.0.0.1, 0.0.0.0")
            .build(),
        OptionalPluginConfigurationParameter.builder()
            .withParameterName(INFLUXDB_PORT)
            .withDescription("Port of influxdb server")
            .withValueType(Integer.class)
            .withExample("8086")
            .withDefaultValue(8086)
            .build(),
        RequiredPluginConfigurationParameter.builder()
            .withParameterName(INFLUXDB_DATABASE)
            .withDescription("Name of the influxdb database")
            .withValueType(String.class)
            .withExample("rika2mqtt")
            .build(),
        OptionalPluginConfigurationParameter.builder()
            .withParameterName(INFLUXDB_PROTOCOL)
            .withDescription("Protocol used to communicate with influxdb")
            .withValueType(String.class)
            .withExample("http")
            .withDefaultValue("http")
            .build(),
        RequiredPluginConfigurationParameter.builder()
            .withParameterName(INFLUXDB_AUTHENTICATION_TOKEN)
            .withDescription("Auth token for influxdb database")
            .withValueType(String.class)
            .build());
  }

  @Override
  public void onConfigurationLoaded(PluginConfiguration pluginConfiguration) {
    //    log.atInfo().log(pluginConfiguration.toString());
    this.pluginConfiguration = pluginConfiguration;
    System.out.println("aaaaaa");
    System.out.println("aaaaaa");
    System.out.println("aaaaaa");
    System.out.println("aaaaaa");
    System.out.println("aaaaaa");
    System.out.println("aaaaaa");
    System.out.println("aaaaaa");
    System.out.println("aaaaaa");
    System.out.println("aaaaaa");
  }
}
