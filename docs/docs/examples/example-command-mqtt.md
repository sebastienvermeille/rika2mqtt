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


## Update convection fans status via MQTT
```json
"stoveId": "your stove id",
"convectionFan1Active": true,
"convectionFan2Active": false
```

## Update heating times via MQTT
```
"stoveId": "your stove id",
"heatingTimeMon1": "<timerange>",
"heatingTimeMon2": "<timerange>",
"heatingTimeTue1": "<timerange>",
"heatingTimeTue2": "<timerange>",
"heatingTimeWed1": "<timerange>",
"heatingTimeWed2": "<timerange>",
"heatingTimeThu1": "<timerange>",
"heatingTimeThu2": "<timerange>",
"heatingTimeFri1": "<timerange>",
"heatingTimeFri2": "<timerange>",
"heatingTimeSat1": "<timerange>",
"heatingTimeSat2": "<timerange>",
"heatingTimeSun1": "<timerange>",
"heatingTimeSun2": "<timerange>",
```

:::info
replace `<timerange>` with i.e: `10302215` = 10h30 to 22h15
:::

You can update only what interest you, you are not forced to provide all properties.
