package dev.cookiecode.rika2mqtt.plugins.internal.v1.mapper;

import static com.google.common.collect.ImmutableList.of;
import static java.time.DayOfWeek.*;
import static org.mapstruct.ReportingPolicy.IGNORE;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.Controls;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.TimeRange;
import java.time.DayOfWeek;
import java.util.List;
import lombok.NonNull;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
    unmappedTargetPolicy = IGNORE,
    uses = {TimeRangeMapper.class, ConvectionFanMapper.class}) // ignore as we are using a map
public interface ControlsMapper {
  @Mapping(source = "onOff", target = "on")
  @Mapping(source = "ecoMode", target = "ecoModeEnabled")
  Controls toApiControls(@NonNull dev.cookiecode.rika2mqtt.rika.firenet.model.Controls controls);

  @AfterMapping
  default void afterMapping(
      dev.cookiecode.rika2mqtt.rika.firenet.model.Controls source,
      @MappingTarget Controls.ControlsBuilder target) {

    // init mappers
    final var convectionFanMapper = new ConvectionFanMapperImpl();
    final var timeRangeMapper = new TimeRangeMapperImpl();

    // Convection fan API Object Oriented way
    final var fan1 =
        convectionFanMapper.map(
            1,
            source.getConvectionFan1Active(),
            source.getConvectionFan1Level(),
            source.getConvectionFan1Area());
    final var fan2 =
        convectionFanMapper.map(
            2,
            source.getConvectionFan2Active(),
            source.getConvectionFan2Level(),
            source.getConvectionFan2Area());

    final var fans = of(fan1, fan2);
    target.fans(fans);

    // Heating times API Object Oriented way
    final var heatingTimes =
        ImmutableMap.<DayOfWeek, List<TimeRange>>builder()
            .put(
                MONDAY,
                of(
                    timeRangeMapper.map(source.getHeatingTimeMon1()),
                    timeRangeMapper.map(source.getHeatingTimeMon2())))
            .put(
                TUESDAY,
                of(
                    timeRangeMapper.map(source.getHeatingTimeTue1()),
                    timeRangeMapper.map(source.getHeatingTimeTue2())))
            .put(
                WEDNESDAY,
                of(
                    timeRangeMapper.map(source.getHeatingTimeWed1()),
                    timeRangeMapper.map(source.getHeatingTimeWed2())))
            .put(
                THURSDAY,
                of(
                    timeRangeMapper.map(source.getHeatingTimeThu1()),
                    timeRangeMapper.map(source.getHeatingTimeThu2())))
            .put(
                FRIDAY,
                of(
                    timeRangeMapper.map(source.getHeatingTimeFri1()),
                    timeRangeMapper.map(source.getHeatingTimeFri2())))
            .put(
                SATURDAY,
                of(
                    timeRangeMapper.map(source.getHeatingTimeSat1()),
                    timeRangeMapper.map(source.getHeatingTimeSat2())))
            .put(
                SUNDAY,
                of(
                    timeRangeMapper.map(source.getHeatingTimeSun1()),
                    timeRangeMapper.map(source.getHeatingTimeSun2())))
            .build();

    target.heatingTimes(heatingTimes);

    // debugs API Object Oriented way
    var debugs =
        ImmutableList.of(
            source.getDebug0(),
            source.getDebug1(),
            source.getDebug2(),
            source.getDebug3(),
            source.getDebug4());

    target.debugs(debugs);

    // patch: for some reason mapstruct do not autodetect
    // probably do to a property called "set" ... :/
    target.setBackTemperature(source.getSetBackTemperature());
  }
}
