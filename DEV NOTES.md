TODO:
* open a PR to PF4J there is an issue:

                        <manifestEntries>
                            <Plugin-Class>dev.cookiecode.rika2mqtt.plugins.influxdb.metrics.Rika2MqttInfluxMetricsPlugin</Plugin-Class>
                            <Plugin-Id>influx-metrics-plugin</Plugin-Id>
                            <Plugin-Version>0.0.1</Plugin-Version>
                            <Plugin-Requires>2.0.0</Plugin-Requires>
                            <Plugin-Description>Export RIKA stoves values to InfluxDB.</Plugin-Description>
                            <Plugin-Provider>Sebastien Vermeille</Plugin-Provider>
                            <Plugin-License>MIT</Plugin-License>
                            <Plugin-Dependencies></Plugin-Dependencies> # Why am I forced to define this... nothing should mean = empty by default it suppose it is x,y,z open a PR to fix it
                        </manifestEntries>

If I do not declare any plugin dependencies entry, when the plugin is loaded it fails saying there is not plugin id.

Declaring it empty might be a wish but then the error is wrong
IMHO should not be forced to declare dependencies if there are none. (See with authors)
