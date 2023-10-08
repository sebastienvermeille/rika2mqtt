package dev.cookiecode.rika2mqtt.plugins.influxdb.metrics;

import com.typesafe.config.ConfigFactory;
import dev.cookiecode.rika2mqtt.plugins.api.Rika2MqttPlugin;
import java.util.HashMap;
import kamon.Kamon;

/** A plugin to export rika2mqtt metrics to InfluxDB */
public class Rika2MqttInfluxMetricsPlugin extends Rika2MqttPlugin {

  @Override
  public void start() {
    // This method is called by the application when the plugin is started.
    System.out.println("Started plugin influxmetrics !");
    //    Config config = ConfigFactory.load("application.conf");

    //    var mergedConfig = config.withFallback(ConfigFactory.defaultReference());

    Kamon.init();

    var defaultConfig = Kamon.config();

    //    var codeConfig = ConfigFactory.parseString("kamon.influxdb.port =  8086");
    //    var codeConfig2 = ConfigFactory.parseString("kamon.influxdb.hostname =  0.0.0.0");
    //    var codeConfig3 = ConfigFactory.parseString("kamon.influxdb.database = mydb");

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
    //    Kamon.initWithoutAttaching(ConfigFactory.defaultReference());
    //
    //    InfluxDBReporter influxDBReporter = new InfluxDBReporter();
    //    Kamon.addReporter("influxdb", influxDBReporter);
  }

  @Override
  public void stop() {
    // This method is called by the application when the plugin is stopped.
  }

  @Override
  public void delete() {
    // This method is called by the application when the plugin is deleted.
  }
}
