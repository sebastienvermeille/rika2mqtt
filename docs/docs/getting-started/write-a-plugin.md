---
sidebar_position: 3
sidebar_label: Plugin development
---

# Write a plugin for RIKA2MQTT

> API for plugins is currently under development and is marked `@Beta`. Be cautious that it can have breaking changes between future releases.

RIKA2MQTT provide an API for plugin creators.

A few modules in the project are demonstrating plugins:

* rika2mqtt-example-plugin
* rika2mqtt-example-plugin-using-config

And an official plugin `rika2mqtt-flux-metrics-plugin` that exports RIKA stove status to InfluxDB.


More documentation will follow in the future when the API is more stable and heavily tested.


## Make a plugin configurable by the end user
Make your plugin main class implement `ConfigurablePlugin`

### Define plugin parameters
Implement: `List<PluginConfigurationParameter> declarePluginConfigurationParameters()`
Parameters can be built using RequiredPluginConfigurationParameter.builder() or OptionalPluginConfigurationParameter.builder() that provide guided assistance.

### Retrieve user defined parameters for the plugin

```java
class YourPlugin extends Rika2MqttPlugin {

    // some code ...

    start(){

        var influxPort = getPluginConfigurationParameter(INFLUXDB_PORT);

    }


}
```

> Note: If a parameter is named: INFLUXDB_PORT, an ENV property will have to be passed to the rika2mqtt docker named `PLUGIN_INFLUXDB_PORT=8086` (it must add an extra `PLUGIN_` this help to keep plugin properties separated.)

Voila!
