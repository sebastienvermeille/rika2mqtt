#!/usr/bin/env bash
# Init script for rika2mqtt

# Run it
java --add-opens=java.base/java.net=ALL-UNNAMED \
  --add-opens=java.base/java.io=ALL-UNNAMED \
  --add-opens=java.base/java.nio=ALL-UNNAMED \
  -jar rika2mqtt.jar