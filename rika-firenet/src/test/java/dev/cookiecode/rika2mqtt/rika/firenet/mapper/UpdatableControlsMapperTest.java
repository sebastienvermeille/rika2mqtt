/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package dev.cookiecode.rika2mqtt.rika.firenet.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import dev.cookiecode.rika2mqtt.rika.firenet.model.Controls;
import dev.cookiecode.rika2mqtt.rika.firenet.model.UpdatableControls.Fields;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/** Test class */
@SpringBootTest(classes = {UpdatableControlsMapperImpl.class})
class UpdatableControlsMapperTest {

  @Autowired private UpdatableControlsMapper mapper;

  @Test
  void toUpdateControlsShouldProperlyMapPropertiesFromControlModel() {

    // GIVEN
    final var revision = 42L;
    final var onOff = true;
    final var operatingMode = 2;
    final var heatingPower = 50;
    final var targetTemperature = 20;
    final var bakeTemperature = 300;
    final var setBackTemperature = 16;
    final var convectionFan1Active = true;
    final var convectionFan1Level = 20;
    final var convectionFan1Area = 14;
    final var convectionFan2Active = true;
    final var convectionFan2Level = 20;
    final var convectionFan2Area = 14;
    final var frostProtectionActive = true;
    final var frostProtectionTemperature = 15;
    final var heatingTimesActiveForComfort = true;
    final var controls =
        Controls.builder()
            .revision(revision)
            .onOff(onOff)
            .operatingMode(operatingMode)
            .heatingPower(heatingPower)
            .targetTemperature(targetTemperature)
            .bakeTemperature(bakeTemperature)
            .setBackTemperature(setBackTemperature)
            .convectionFan1Active(convectionFan1Active)
            .convectionFan1Level(convectionFan1Level)
            .convectionFan1Area(convectionFan1Area)
            .convectionFan2Active(convectionFan2Active)
            .convectionFan2Level(convectionFan2Level)
            .convectionFan2Area(convectionFan2Area)
            .frostProtectionActive(frostProtectionActive)
            .frostProtectionTemperature(frostProtectionTemperature)
            .heatingTimesActiveForComfort(heatingTimesActiveForComfort)
            .build();

    // WHEN
    final var actualUpdateControls = mapper.toUpdateControls(controls);

    // THEN
    assertThat(actualUpdateControls).hasNoNullFieldsOrProperties();
  }

  @Test
  void mergeWithMapShouldOverrideDtoWithMapValues() {

    // GIVEN
    final var revision = 42L;
    final var onOff = true;
    final var operatingMode = 2;
    final var heatingPower = 50;
    final var targetTemperature = 20;
    final var bakeTemperature = 300;
    final var setBackTemperature = 16;
    final var convectionFan1Active = true;
    final var convectionFan1Level = 20;
    final var convectionFan1Area = 14;
    final var convectionFan2Active = true;
    final var convectionFan2Level = 20;
    final var convectionFan2Area = 14;
    final var frostProtectionActive = true;
    final var frostProtectionTemperature = 15;
    final var heatingTimesActiveForComfort = true;

    final var controls =
        Controls.builder()
            .revision(revision)
            .onOff(onOff)
            .operatingMode(operatingMode)
            .heatingPower(heatingPower)
            .targetTemperature(targetTemperature)
            .bakeTemperature(bakeTemperature)
            .setBackTemperature(setBackTemperature)
            .convectionFan1Active(convectionFan1Active)
            .convectionFan1Level(convectionFan1Level)
            .convectionFan1Area(convectionFan1Area)
            .convectionFan2Active(convectionFan2Active)
            .convectionFan2Level(convectionFan2Level)
            .convectionFan2Area(convectionFan2Area)
            .frostProtectionActive(frostProtectionActive)
            .frostProtectionTemperature(frostProtectionTemperature)
            .heatingTimesActiveForComfort(heatingTimesActiveForComfort)
            .build();

    var updateControls = mapper.toUpdateControls(controls);

    final var expectedRevision = 99;
    // TODO: this test only one property... should be for all but not at once -> parametrized tests
    // might help
    final var fields = new HashMap<String, String>();
    fields.put(Fields.REVISION, String.valueOf(expectedRevision));

    // WHEN
    mapper.mergeWithMap(fields, updateControls);

    // THEN
    assertThat(updateControls.getRevision()).isEqualTo(expectedRevision);
  }
}
