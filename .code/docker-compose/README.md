# Docker Compose

If you want to develop this project, docker-compose might become your friend.
I use it in order to be able to test my development locally without having to deploy it on my
production mqtt server.

Just run the docker-compose.yml file `docker-compose up -d` and then you get an mqtt server:

```
MQTT_HOST=localhost
MQTT_USER=testuser
MQTT_PASSWORD=secret
```

To be safe you can create a file named `docker-compose.override.yml`:

```docker-compose.override.yml
rika2mqtt:
  environment:
    - RIKA_EMAIL=<your email>
    - RIKA_PASSWORD=<your password>
```

This file is git ignored.

## Testing MQTT

I use mqtt-explorer (https://mqtt-explorer.com/) to be able to read mqtt messages and I connect it
to the docker-compose mqtt server.
Then I can debug the app and send commands on topic `cmnd/rika2mqtt`

With body:

```json
"stoveID": <put yours>,
"operatingMode": 2,
"heatingPower": 70,
"targetTemperature": 18,
"bakeTemperature": 340,
"onOff": true,
"heatingTimesActiveForComfort": false,
"setBackTemperature": 13,
"convectionFan1Active": true,
"convectionFan1Level": 0,
"convectionFan1Area": 12,
"convectionFan2Active": true,
"convectionFan2Level": 0,
"convectionFan2Area": 12,
"frostProtectionActive": true,
"frostProtectionTemperature": 10,
"revision": 1684317751
```