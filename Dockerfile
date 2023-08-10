FROM eclipse-temurin:20.0.2_9-jre-jammy

RUN mkdir -p /opt/rika2mqtt

WORKDIR /opt/rika2mqtt
COPY .docker/ .
COPY bridge/target/*.jar rika2mqtt.jar
RUN sh -c 'touch rika2mqtt.jar'

RUN chmod a+x init.sh

# set the startup command to run
ENTRYPOINT ["/opt/rika2mqtt/init.sh"]