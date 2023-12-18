package dev.cookiecode.rika2mqtt.plugins.internal.v1.mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

import dev.cookiecode.rika2mqtt.plugins.api.v1.model.ConvectionFan;
import lombok.NonNull;
import org.mapstruct.Mapper;

@Mapper(unmappedTargetPolicy = IGNORE) // ignore as we are using a map
public interface ConvectionFanMapper {

  default ConvectionFan map(
      @NonNull Integer identifier,
      @NonNull Boolean active,
      @NonNull Integer level,
      @NonNull Integer area) {
    return ConvectionFan.builder()
        .identifier(identifier)
        .active(active)
        .level(level)
        .area(area)
        .build();
  }
}
