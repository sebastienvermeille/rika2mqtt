package ch.svermeille.rika.mqtt.configuration;

import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * @author Sebastien Vermeille
 */
@Configuration
@EnableConfigurationProperties(MqttConfigProperties.class)
@IntegrationComponentScan
@RequiredArgsConstructor
@EnableIntegration
public class MqttConfiguration {

  private final MqttConfigProperties mqttConfigProperties;

  @Bean
  public MqttPahoClientFactory mqttClientFactory() {
    DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
    MqttConnectOptions options = new MqttConnectOptions();
    options.setServerURIs(new String[] {"tcp://" + mqttConfigProperties.getHost() + ":" + mqttConfigProperties.getPort()});
    options.setUserName(mqttConfigProperties.getUser());
    options.setPassword(mqttConfigProperties.getPassword().toCharArray());
    factory.setConnectionOptions(options);
    return factory;
  }

  @Bean
  @ServiceActivator(inputChannel = "mqttOutboundChannel")
  public MessageHandler mqttOutbound() {
    var messageHandler = new MqttPahoMessageHandler(mqttConfigProperties.getClientName(), mqttClientFactory());
    messageHandler.setAsync(true);
    messageHandler.setDefaultTopic(mqttConfigProperties.getTelemetryReportTopicName());
    return messageHandler;
  }

  /**
   * @implNote this is using a workaround found here: https://stackoverflow.com/a/41241824 the doc
   * simply mention to do: `return new DirectChannel();`
   */
  @Bean
  public MessageChannel mqttOutboundChannel() {
    var dc = new DirectChannel();
    dc.subscribe(mqttOutbound());
    return dc;
  }


  @MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
  public interface MqttGateway {
    void sendToMqtt(String data);

  }
}
