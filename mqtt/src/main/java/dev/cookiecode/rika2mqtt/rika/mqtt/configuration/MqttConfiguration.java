/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package dev.cookiecode.rika2mqtt.rika.mqtt.configuration;

import dev.cookiecode.rika2mqtt.rika.mqtt.event.MqttCommandEvent;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

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
  private final ApplicationEventPublisher applicationEventPublisher;


  @Bean
  public MqttPahoClientFactory mqttClientFactory() {
    DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
    MqttConnectOptions options = new MqttConnectOptions();
    options.setServerURIs(new String[]{
        "tcp://" + mqttConfigProperties.getHost() + ":" + mqttConfigProperties.getPort()});
    options.setUserName(mqttConfigProperties.getUsername());
    options.setPassword(mqttConfigProperties.getPassword().toCharArray());
    factory.setConnectionOptions(options);
    return factory;
  }


  @Bean
  @ServiceActivator(inputChannel = "mqttOutboundChannel", autoStartup = "true")
  public MessageHandler mqttOutbound() {
    var messageHandler = new MqttPahoMessageHandler(mqttConfigProperties.getClientName(),
        mqttClientFactory());
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

  @Bean
  public MessageChannel mqttInputChannel() {
    return new DirectChannel();
  }

  @Bean
  public MessageProducer inbound() {
    final var adapter = new MqttPahoMessageDrivenChannelAdapter(
        mqttConfigProperties.getClientName() + "_writer",
        mqttClientFactory(),
        mqttConfigProperties.getCommandTopicName()
    );
    adapter.setCompletionTimeout(5000);
    adapter.setConverter(new DefaultPahoMessageConverter());
    adapter.setQos(1);
    adapter.setOutputChannel(mqttInputChannel());
    return adapter;
  }

  @Bean
  @ServiceActivator(inputChannel = "mqttInputChannel")
  public MessageHandler handler() {
    return new MessageHandler() {

      @Override
      public void handleMessage(Message<?> message) throws MessagingException {
        applicationEventPublisher.publishEvent(new MqttCommandEvent("it works"));
      }

    };
  }
}
