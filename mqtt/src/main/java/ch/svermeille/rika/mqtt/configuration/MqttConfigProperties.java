package ch.svermeille.rika.mqtt.configuration;

import ch.svermeille.rika.shared.RequireRestartWhenChanged;
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

  @RequireRestartWhenChanged
  @NotEmpty
  private String host;

  @RequireRestartWhenChanged
  @NotNull
  private Integer port = 1883;

  @RequireRestartWhenChanged
  private String user;

  @RequireRestartWhenChanged
  private String password;

  @RequireRestartWhenChanged
  private String clientName = "rika2mqtt";

  @RequireRestartWhenChanged
  private String telemetryReportTopicName = "tele/rika2mqtt";

  @RequireRestartWhenChanged
  private String commandTopicName = "cmnd/rika2mqtt";
}
