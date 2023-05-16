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