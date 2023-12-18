package dev.cookiecode.rika2mqtt.plugins.internal.v1.mapper;

import dev.cookiecode.rika2mqtt.plugins.api.v1.model.StoveStatus;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {SensorsMapper.class, ControlsMapper.class}) // ignore as we are using a map
public interface StoveStatusMapper {

  StoveStatus toApiStoveStatus(
      @NonNull dev.cookiecode.rika2mqtt.rika.firenet.model.StoveStatus stoveStatus);
}
