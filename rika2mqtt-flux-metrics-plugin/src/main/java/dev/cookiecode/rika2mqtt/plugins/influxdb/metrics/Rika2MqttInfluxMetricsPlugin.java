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

import com.typesafe.config.ConfigFactory;
import dev.cookiecode.rika2mqtt.plugins.api.v1.Rika2MqttPlugin;
import java.util.HashMap;
import kamon.Kamon;

/** A plugin to export rika2mqtt metrics to InfluxDB */
public class Rika2MqttInfluxMetricsPlugin extends Rika2MqttPlugin {

  @Override
  public void start() {
    Kamon.init();

    var defaultConfig = Kamon.config();

    // TODO: have to be customizable
    var props = new HashMap<String, Object>();
    props.put("kamon.influxdb.port", 8086);
    props.put("kamon.influxdb.hostname", "0.0.0.0");
    props.put("kamon.influxdb.database", "rika2mqtt");
    props.put("kamon.influxdb.protocol", "http");
    props.put("kamon.influxdb.authentication.token", "admin-token");

    var codeConfig = ConfigFactory.parseMap(props);
    var newConfig = codeConfig.withFallback(defaultConfig);
    Kamon.reconfigure(newConfig);
    Kamon.loadModules();
  }

  @Override
  public void stop() {
    log.atInfo().log("Stopping Kamon...");
    Kamon.stop();
  }
}
