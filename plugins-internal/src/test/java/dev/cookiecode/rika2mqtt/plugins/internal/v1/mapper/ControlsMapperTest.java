package dev.cookiecode.rika2mqtt.plugins.internal.v1.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import dev.cookiecode.rika2mqtt.rika.firenet.model.Controls;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/** Test class */
@SpringBootTest(
    classes = {ControlsMapperImpl.class, TimeRangeMapperImpl.class, ConvectionFanMapperImpl.class})
class ControlsMapperTest {

  @Autowired private ControlsMapper mapper;

  @Test
  void mapShouldProperlyMapGivenProperties() {

    // GIVEN
    final var on = true;
    final var revision = 1232132L;
    final var operatingMode = 1;
    final var heatingPower = 80;
    final var targetTemperature = 19;
    final var bakeTemperature = 20;
    final var ecoMode = true;
    final var fan1Active = true;
    final var fan1Area = 50;
    final var fan1Level = 12;
    final var fan2Active = false;
    final var fan2Area = 54;
    final var fan2Level = 13;
    final var frostProtection = true;
    final var debug0 = 0;
    final var debug1 = 1;
    final var debug2 = 2;
    final var debug3 = 3;
    final var debug4 = 4;
    final Controls controls =
        Controls.builder()
            .onOff(on)
            .revision(revision)
            .operatingMode(operatingMode)
            .heatingPower(heatingPower)
            .targetTemperature(targetTemperature)
            .bakeTemperature(bakeTemperature)
            .ecoMode(ecoMode)
            .convectionFan1Active(fan1Active)
            .convectionFan1Area(fan1Area)
            .convectionFan1Level(fan1Level)
            .convectionFan2Active(fan2Active)
            .convectionFan2Area(fan2Area)
            .convectionFan2Level(fan2Level)
            .frostProtectionActive(frostProtection)
            // TODO: heating time are provided here because could not mock the mapper
            // at the moment. The conversion will not be checked in this class however)
            .heatingTimeMon1("08001200")
            .heatingTimeMon2("12041800")
            .heatingTimeTue1("08001200")
            .heatingTimeTue2("12041800")
            .heatingTimeWed1("08001200")
            .heatingTimeWed2("12041800")
            .heatingTimeThu1("08001200")
            .heatingTimeThu2("12041800")
            .heatingTimeFri1("08001200")
            .heatingTimeFri2("12041800")
            .heatingTimeSat1("08001200")
            .heatingTimeSat2("12041800")
            .heatingTimeSun1("08001200")
            .heatingTimeSun2("12041800")
            .debug0(debug0)
            .debug1(debug1)
            .debug2(debug2)
            .debug3(debug3)
            .debug4(debug4)
            .build();

    // WHEN
    final var result = mapper.toApiControls(controls);

    // THEN
    assertThat(result.isOn()).isEqualTo(on);
    assertThat(result.getRevision()).isEqualTo(revision);
    assertThat(result.getOperatingMode()).isEqualTo(operatingMode);
    assertThat(result.getHeatingPower()).isEqualTo(heatingPower);
    assertThat(result.getTargetTemperature()).isEqualTo(targetTemperature);
    assertThat(result.getBakeTemperature()).isEqualTo(bakeTemperature);
    assertThat(result.isEcoModeEnabled()).isEqualTo(ecoMode);

    assertThat(result.getFans().get(0).isActive()).isEqualTo(fan1Active);
    assertThat(result.getFans().get(1).isActive()).isEqualTo(fan2Active);
  }
}
