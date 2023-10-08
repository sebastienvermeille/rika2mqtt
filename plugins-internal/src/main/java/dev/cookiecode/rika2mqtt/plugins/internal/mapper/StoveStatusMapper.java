package dev.cookiecode.rika2mqtt.plugins.internal.mapper;

import dev.cookiecode.rika2mqtt.plugins.api.model.StoveStatus;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE) // ignore as we are using a map
public interface StoveStatusMapper {

  StoveStatus toApiStoveStatus(
      @NonNull dev.cookiecode.rika2mqtt.rika.firenet.model.StoveStatus stoveStatus);
}
