package dev.cookiecode.rika2mqtt.plugins.internal.v1.mapper.helper;

import dev.cookiecode.rika2mqtt.plugins.api.v1.model.Sensors;
import dev.cookiecode.rika2mqtt.plugins.internal.v1.mapper.SensorsMapper;
import lombok.NonNull;
import org.springframework.stereotype.Component;

/**
 * This class is intentionally very empty (It allows to keep tests valuable and not repeating
 * themselves)
 */
@Component
public class SensorsMapperEmptyImpl implements SensorsMapper {

  @Override
  public Sensors toApiSensors(
      dev.cookiecode.rika2mqtt.rika.firenet.model.@NonNull Sensors sensors) {
    return Sensors.builder().build();
  }
}
