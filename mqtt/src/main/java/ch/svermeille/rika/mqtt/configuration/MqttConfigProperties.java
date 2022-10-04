package ch.svermeille.rika.mqtt.configuration;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import top.code2life.config.DynamicConfig;

/**
 * @author Sebastien Vermeille
 */
@Configuration
@DynamicConfig
@ConfigurationProperties(prefix = "mqtt")
@Data
@Validated
public class MqttConfigProperties {

  @NotEmpty
  private String host;

  @NotNull
  private Integer port = 1883;

  private String user;

  private String password;

  private String clientName = "rika2mqtt";

  private String telemetryReportTopicName = "tele/rika2mqtt";

  private String commandTopicName = "cmnd/rika2mqtt";
}
