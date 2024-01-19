---
sidebar_position: 2
---

# Configuration

## RIKA_EMAIL
The email of your rika-firenet account

## RIKA_PASSWORD
The password of your rika-firenet account

## MQTT_HOST
The host of your MQTT instance (ip or fqdn)

## MQTT_USERNAME
The user to use to connect to MQTT (optional depends on your mqtt config)

## MQTT_PASSWORD
The password associated to the user defined previously in MQTT_USER (optional depends on your mqtt config)

## MQTT_PORT (Optional)
The MQTT server port
Default: 1883

## MQTT_COMMAND_TOPIC_NAME (Optional)
The MQTT topic used to send command to RIKA2MQTT
Default: `cmnd/rika2mqtt`

## MQTT_TELEMETRY_REPORT_TOPIC_NAME (Optional)
The MQTT topic used by RIKA2MQTT to publish RIKA status
Default: `tele/rika2mqtt`

## MQTT_ERROR_TOPIC_NAME (Optional)
The MQTT topic used by RIKA2MQTT to publish RIKA errors (i.e. Empty pellet container)
Default: `tele/rika2mqtt-errors`

## MQTT_URI_SCHEME (Optional)
The uri scheme to be used with MQTT_HOST (i.e: `tcp://`, `ssl://`)
Default: `tcp://`
