package dev.cookiecode.rika2mqtt.plugins.internal.v1.mapper.helper;

import dev.cookiecode.rika2mqtt.plugins.api.v1.model.Controls;
import dev.cookiecode.rika2mqtt.plugins.internal.v1.mapper.ControlsMapper;
import lombok.NonNull;
import org.springframework.stereotype.Component;

/**
 * This class is intentionally very empty (It allows to keep tests valuable and not repeating
 * themselves)
 */
@Component
public class ControlsMapperEmptyImpl implements ControlsMapper {

  @Override
  public Controls toApiControls(
      dev.cookiecode.rika2mqtt.rika.firenet.model.@NonNull Controls controls) {
    return Controls.builder().build();
  }
}
