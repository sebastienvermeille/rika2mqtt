package dev.cookiecode.rika2mqtt.plugins.internal.mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

import dev.cookiecode.rika2mqtt.plugins.api.model.Controls;
import lombok.NonNull;
import org.mapstruct.Mapper;

@Mapper(unmappedTargetPolicy = IGNORE) // ignore as we are using a map
public interface ControlsMapper {
  Controls toApiControls(@NonNull dev.cookiecode.rika2mqtt.rika.firenet.model.Controls controls);
}
