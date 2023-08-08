/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package dev.cookiecode.rika2mqtt.rika.firenet.mapper;

import static java.util.Optional.ofNullable;
import static org.mapstruct.ReportingPolicy.IGNORE;

import dev.cookiecode.rika2mqtt.rika.firenet.model.Controls;
import dev.cookiecode.rika2mqtt.rika.firenet.model.UpdatableControls;
import dev.cookiecode.rika2mqtt.rika.firenet.model.UpdatableControls.Fields;
import java.util.Map;
import lombok.NonNull;
import org.mapstruct.Mapper;

@Mapper(unmappedTargetPolicy = IGNORE) // ignore as we are using a map
public interface UpdatableControlsMapper {

  UpdatableControls toUpdateControls(@NonNull Map<String, String> map);

  UpdatableControls toUpdateControls(@NonNull Controls controls);

  default void mergeWithMap(@NonNull Map<String, String> fields,
      @NonNull UpdatableControls currentControls) {

    ofNullable(fields.getOrDefault(Fields.REVISION, null))
        .ifPresent(value -> currentControls.setRevision(Long.valueOf(value)));

    ofNullable(fields.getOrDefault(Fields.OPERATING_MODE, null))
        .ifPresent(value -> currentControls.setOperatingMode(Integer.valueOf(value)));

    ofNullable(fields.getOrDefault(Fields.HEATING_POWER, null))
        .ifPresent(value -> currentControls.setHeatingPower(Integer.valueOf(value)));

    ofNullable(fields.getOrDefault(Fields.TARGET_TEMPERATURE, null))
        .ifPresent(value -> currentControls.setTargetTemperature(Integer.valueOf(value)));

    ofNullable(fields.getOrDefault(Fields.BAKE_TEMPERATURE, null))
        .ifPresent(value -> currentControls.setBakeTemperature(Integer.valueOf(value)));

    ofNullable(fields.getOrDefault(Fields.HEATING_TIMES_ACTIVE_FOR_COMFORT, null))
        .ifPresent(
            value -> currentControls.setHeatingTimesActiveForComfort(Boolean.valueOf(value)));

    ofNullable(fields.getOrDefault(Fields.SET_BACK_TEMPERATURE, null))
        .ifPresent(value -> currentControls.setSetBackTemperature(Integer.valueOf(value)));

    ofNullable(fields.getOrDefault(Fields.CONVECTION_FAN1_ACTIVE, null))
        .ifPresent(value -> currentControls.setConvectionFan1Active(Boolean.valueOf(value)));

    ofNullable(fields.getOrDefault(Fields.CONVECTION_FAN1_AREA, null))
        .ifPresent(value -> currentControls.setConvectionFan1Area(Integer.valueOf(value)));

    ofNullable(fields.getOrDefault(Fields.CONVECTION_FAN1_LEVEL, null))
        .ifPresent(value -> currentControls.setConvectionFan1Level(Integer.valueOf(value)));

    ofNullable(fields.getOrDefault(Fields.CONVECTION_FAN2_ACTIVE, null))
        .ifPresent(value -> currentControls.setConvectionFan2Active(Boolean.valueOf(value)));

    ofNullable(fields.getOrDefault(Fields.CONVECTION_FAN2_AREA, null))
        .ifPresent(value -> currentControls.setConvectionFan2Area(Integer.valueOf(value)));

    ofNullable(fields.getOrDefault(Fields.CONVECTION_FAN2_LEVEL, null))
        .ifPresent(value -> currentControls.setConvectionFan2Level(Integer.valueOf(value)));

    ofNullable(fields.getOrDefault(Fields.FROST_PROTECTION_ACTIVE, null))
        .ifPresent(value -> currentControls.setFrostProtectionActive(Boolean.valueOf(value)));

    ofNullable(fields.getOrDefault(Fields.FROST_PROTECTION_TEMPERATURE, null))
        .ifPresent(value -> currentControls.setFrostProtectionTemperature(Integer.valueOf(value)));

  }

}
