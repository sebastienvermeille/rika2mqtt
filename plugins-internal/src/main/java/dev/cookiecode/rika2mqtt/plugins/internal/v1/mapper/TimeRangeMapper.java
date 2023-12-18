package dev.cookiecode.rika2mqtt.plugins.internal.v1.mapper;

import static java.lang.Integer.parseInt;
import static org.mapstruct.ReportingPolicy.IGNORE;

import dev.cookiecode.rika2mqtt.plugins.api.v1.model.TimeDefinition;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.TimeRange;
import lombok.NonNull;
import org.mapstruct.Mapper;

@Mapper(unmappedTargetPolicy = IGNORE) // ignore as we are using a map
public interface TimeRangeMapper {
  /**
   * @param rikaTimeRange i.e: `13302200`
   * @return a TimeRange from 13:30 to 22:00
   */
  default TimeRange map(@NonNull String rikaTimeRange) {

    var from = rikaTimeRange.substring(0, 4);
    var to = rikaTimeRange.substring(4);
    return TimeRange.builder()
        .from(
            TimeDefinition.builder()
                .hours(parseInt(from.substring(0, 2)))
                .minutes(parseInt(from.substring(2)))
                .build())
        .to(
            TimeDefinition.builder()
                .hours(parseInt(to.substring(0, 2)))
                .minutes(parseInt(to.substring(2)))
                .build())
        .build();
  }
}
