# rika2mqtt

Bridge to let you take control of your RIKA firenet stove through MQTT

# Run it via docker

```
docker run \
    -e RIKA_EMAIL=<yourRikaFirenetAccount> \
    -e RIKA_PASSWORD=<yourPassword> \
    -e MQTT_HOST=127.0.0.1 \
    -e MQTT_USER=<optional> \
    -e MQTT_PASSWORD=<optional> \
    -d --name rika2mqtt sbeex/rika2mqtt:latest
```
