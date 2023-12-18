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
