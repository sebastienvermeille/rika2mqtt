# RIKA2MQTT

![logo](./docs/assets/rika2mqtt-128x128.png)

RIKA2MQTT is a free and opensource bridge enabling end-users to connect their RIKA firenet stove to an MQTT server.
It goes both way so you can monitor and also take control of your stove directly via MQTT.

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/dev.cookiecode/rika2mqtt-parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/dev.cookiecode/rika2mqtt-parent)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![join discord](https://img.shields.io/badge/join%20discord-gray?style=flat&logo=discord&link=https://discord.gg/uqQ2SWCQCb)](https://discord.gg/uqQ2SWCQCb)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=sebastienvermeille_rika2mqtt&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=sebastienvermeille_rika2mqtt)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=sebastienvermeille_rika2mqtt&metric=coverage)](https://sonarcloud.io/summary/new_code?id=sebastienvermeille_rika2mqtt)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=sebastienvermeille_rika2mqtt&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=sebastienvermeille_rika2mqtt)
[![code style](https://img.shields.io/badge/code%20style-google%20code%20style-green?style=flat&link=https://google.github.io/styleguide/javaguide.html)](https://google.github.io/styleguide/javaguide.html)
[![Known Vulnerabilities](https://snyk.io/test/github/sebastienvermeille/rika2mqtt/badge.svg)](https://snyk.io/test/github/sebastienvermeille/rika2mqtt)
[![OpenSSF Best Practices](https://bestpractices.coreinfrastructure.org/projects/7361/badge)](https://bestpractices.coreinfrastructure.org/projects/7361)

# Run it via docker

```
docker run \
    -e RIKA_EMAIL=<yourRikaFirenetAccount> \
    -e RIKA_PASSWORD=<yourPassword> \
    -e MQTT_HOST=127.0.0.1 \
    -e MQTT_USER=<optional> \
    -e MQTT_PASSWORD=<optional> \
    -d --name rika2mqtt cookiecodedev/rika2mqtt:latest
```

It will publish to the defined mqtt server in topic `tele/rika2mqtt`.

It can consume input commands (mqtt -> rika stove) via topic `cmnd/rika2mqtt`.

## RIKA_EMAIL

The email of your rika-firenet account

## RIKA_PASSWORD

The password of your rika-firenet account

## MQTT_HOST

The host of your MQTT instance (ip or fqdn)

## MQTT_USERNAME

The user to use to connect to MQTT (optional depends on your mqtt config)

## MQTT_PASSWORD

The password associated to the user defined previously in MQTT_USER (optional depends on your mqtt
config)

## MQTT_COMMAND_TOPIC_NAME (Optional)
The MQTT topic used to send command to RIKA2MQTT
Default: `cmnd/rika2mqtt`

## MQTT_TELEMETRY_REPORT_TOPIC_NAME (Optional)
The MQTT topic used by RIKA2MQTT to publish RIKA status
Default: `tele/rika2mqtt`

## MQTT_PORT (Optional)

The port of your MQTT instance
Default: `1883`

## MQTT_URI_SCHEME (Optional)
The uri scheme to use to connect to the MQTT server 
Default: `tcp://`

# Other docs:
* [FAQ](./FAQ.md)
* [Developer doc](./DEV.md)
* [Release doc](./RELEASE.md)

# Credits
Thanks to [Freepik](https://www.freepik.com/icon/pellet-stove_6625341) for providing the pellet stove icon used in the diagram
