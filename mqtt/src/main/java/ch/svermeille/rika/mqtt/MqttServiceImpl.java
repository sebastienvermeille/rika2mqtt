package ch.svermeille.rika.mqtt;

import ch.svermeille.rika.mqtt.configuration.MqttConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

/**
 * @author Sebastien Vermeille
 */
@Service
@RequiredArgsConstructor
@Flogger
public class MqttServiceImpl implements MqttService {

  private final MqttConfiguration.MqttGateway mqttGateway;

  @Override
  public void publish(final String message) {
    log.atInfo().log("Publish %s to mqtt", message);
    mqttGateway.sendToMqtt(message);
  }


}
