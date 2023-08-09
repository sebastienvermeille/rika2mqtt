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
import org.mapstruct.Mapping;

@Mapper(unmappedTargetPolicy = IGNORE) // ignore as we are using a map
public interface UpdatableControlsMapper {

//  @Mapping(source = "setBackTemperature", target = "setBackTemperature")
  UpdatableControls toUpdateControls(@NonNull Controls controls);

  default void mergeWithMap(@NonNull Map<String, String> diffs,
      @NonNull UpdatableControls controlsToUpdate) {

    ofNullable(diffs.getOrDefault(Fields.REVISION, null))
        .ifPresent(value -> controlsToUpdate.setRevision(Long.valueOf(value)));

    ofNullable(diffs.getOrDefault(Fields.OPERATING_MODE, null))
        .ifPresent(value -> controlsToUpdate.setOperatingMode(Integer.valueOf(value)));

    ofNullable(diffs.getOrDefault(Fields.HEATING_POWER, null))
        .ifPresent(value -> controlsToUpdate.setHeatingPower(Integer.valueOf(value)));

    ofNullable(diffs.getOrDefault(Fields.TARGET_TEMPERATURE, null))
        .ifPresent(value -> controlsToUpdate.setTargetTemperature(Integer.valueOf(value)));

    ofNullable(diffs.getOrDefault(Fields.BAKE_TEMPERATURE, null))
        .ifPresent(value -> controlsToUpdate.setBakeTemperature(Integer.valueOf(value)));

    ofNullable(diffs.getOrDefault(Fields.HEATING_TIMES_ACTIVE_FOR_COMFORT, null))
        .ifPresent(
            value -> controlsToUpdate.setHeatingTimesActiveForComfort(Boolean.valueOf(value)));

    ofNullable(diffs.getOrDefault(Fields.SET_BACK_TEMPERATURE, null))
        .ifPresent(value -> controlsToUpdate.setSetBackTemperature(Integer.valueOf(value)));

    ofNullable(diffs.getOrDefault(Fields.CONVECTION_FAN1_ACTIVE, null))
        .ifPresent(value -> controlsToUpdate.setConvectionFan1Active(Boolean.valueOf(value)));

    ofNullable(diffs.getOrDefault(Fields.CONVECTION_FAN1_AREA, null))
        .ifPresent(value -> controlsToUpdate.setConvectionFan1Area(Integer.valueOf(value)));

    ofNullable(diffs.getOrDefault(Fields.CONVECTION_FAN1_LEVEL, null))
        .ifPresent(value -> controlsToUpdate.setConvectionFan1Level(Integer.valueOf(value)));

    ofNullable(diffs.getOrDefault(Fields.CONVECTION_FAN2_ACTIVE, null))
        .ifPresent(value -> controlsToUpdate.setConvectionFan2Active(Boolean.valueOf(value)));

    ofNullable(diffs.getOrDefault(Fields.CONVECTION_FAN2_AREA, null))
        .ifPresent(value -> controlsToUpdate.setConvectionFan2Area(Integer.valueOf(value)));

    ofNullable(diffs.getOrDefault(Fields.CONVECTION_FAN2_LEVEL, null))
        .ifPresent(value -> controlsToUpdate.setConvectionFan2Level(Integer.valueOf(value)));

    ofNullable(diffs.getOrDefault(Fields.FROST_PROTECTION_ACTIVE, null))
        .ifPresent(value -> controlsToUpdate.setFrostProtectionActive(Boolean.valueOf(value)));

    ofNullable(diffs.getOrDefault(Fields.FROST_PROTECTION_TEMPERATURE, null))
        .ifPresent(value -> controlsToUpdate.setFrostProtectionTemperature(Integer.valueOf(value)));

    ofNullable(diffs.getOrDefault(Fields.ON_OFF, null))
        .ifPresent(value -> controlsToUpdate.setOnOff(Boolean.valueOf(value)));
  }
}
