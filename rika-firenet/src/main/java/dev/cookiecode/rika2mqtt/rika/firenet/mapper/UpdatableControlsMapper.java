/*
 * The MIT License
 * Copyright © 2022 Sebastien Vermeille
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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

  //  @Mapping(source = "setBackTemperature", target = "setBackTemperature")
  UpdatableControls toUpdateControls(@NonNull Controls controls);

  default void mergeWithMap(
      @NonNull Map<String, String> diffs, @NonNull UpdatableControls controlsToUpdate) {

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

    ofNullable(diffs.getOrDefault(Fields.HEATING_TIME_MON1, null))
        .ifPresent(controlsToUpdate::setHeatingTimeMon1);
    ofNullable(diffs.getOrDefault(Fields.HEATING_TIME_MON2, null))
        .ifPresent(controlsToUpdate::setHeatingTimeMon2);
    ofNullable(diffs.getOrDefault(Fields.HEATING_TIME_TUE1, null))
        .ifPresent(controlsToUpdate::setHeatingTimeTue1);
    ofNullable(diffs.getOrDefault(Fields.HEATING_TIME_TUE2, null))
        .ifPresent(controlsToUpdate::setHeatingTimeTue2);
    ofNullable(diffs.getOrDefault(Fields.HEATING_TIME_WED1, null))
        .ifPresent(controlsToUpdate::setHeatingTimeWed1);
    ofNullable(diffs.getOrDefault(Fields.HEATING_TIME_WED2, null))
        .ifPresent(controlsToUpdate::setHeatingTimeWed2);
    ofNullable(diffs.getOrDefault(Fields.HEATING_TIME_THU1, null))
        .ifPresent(controlsToUpdate::setHeatingTimeThu1);
    ofNullable(diffs.getOrDefault(Fields.HEATING_TIME_THU2, null))
        .ifPresent(controlsToUpdate::setHeatingTimeThu2);
    ofNullable(diffs.getOrDefault(Fields.HEATING_TIME_FRI1, null))
        .ifPresent(controlsToUpdate::setHeatingTimeFri1);
    ofNullable(diffs.getOrDefault(Fields.HEATING_TIME_FRI2, null))
        .ifPresent(controlsToUpdate::setHeatingTimeFri2);
    ofNullable(diffs.getOrDefault(Fields.HEATING_TIME_SAT1, null))
        .ifPresent(controlsToUpdate::setHeatingTimeSat1);
    ofNullable(diffs.getOrDefault(Fields.HEATING_TIME_SAT2, null))
        .ifPresent(controlsToUpdate::setHeatingTimeSat2);
    ofNullable(diffs.getOrDefault(Fields.HEATING_TIME_SUN1, null))
        .ifPresent(controlsToUpdate::setHeatingTimeSun1);
    ofNullable(diffs.getOrDefault(Fields.HEATING_TIME_SUN2, null))
        .ifPresent(controlsToUpdate::setHeatingTimeSun2);

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
