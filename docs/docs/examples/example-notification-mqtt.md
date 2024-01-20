# Receive RIKA notifications via MQTT

RIKA2MQTT bridge publish errors/warnings displayed on your RIKA screen to MQTT.
This allows you to react when your pellet container is empty by example.

:::info
By default the notifications are published to the topic: `tele/rika2mqtt-notifications` (Or your custom topic defined in `MQTT_NOTIFICATION_TOPIC_NAME`).
:::

The payload is in JSON format :
```
{
  "stoveId": 42,
  "errorCode": "E0001.02",
}
```

## Known error codes

| Code       | Description                 |
|------------|-----------------------------|
| E0001.02   | Pellet container: empty     |
