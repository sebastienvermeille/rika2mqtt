# Control RIKA stove via MQTT

You can send commands to your stove via MQTT. This opens a lot of possibilities for your IoT system, NodeRed, ...


:::info
By default RIKA2MQTT will listen for commands sent in topic: `cmnd/rika2mqtt` (or your custom topic defined in `MQTT_COMMAND_TOPIC_NAME`).
:::

Here are some example of commands that can be sent to your RIKA stove via MQTT using RIKA2MQTT.

## Update target temperature via MQTT

Publish an MQTT message to `cmnd/rika2mqtt`  with the following JSON payload:

```
"stoveId": "your stove id",
"targetTemperature": 20
```

:::info
To retrieve your stove id, simply go to rika-firenet and display your stove. Then you can retrieve the stove id from the url of the browser: https://rika-firenet.com/web/stove/[your stove id]
:::
And voila! RIKA2MQTT will automatically do the necessary to forward this change to Rika-Firenet that will then forward it to your Rika stove.