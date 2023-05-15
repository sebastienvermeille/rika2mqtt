#!/usr/bin/env bash

set -e

echo "Building docker image cookiecodedev/rika2mqtt:latest"

docker build . -t cookiecodedev/rika2mqtt:latest