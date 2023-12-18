package dev.cookiecode.rika2mqtt.plugins.internal.v1.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import dev.cookiecode.rika2mqtt.plugins.internal.v1.mapper.helper.ControlsMapperEmptyImpl;
import dev.cookiecode.rika2mqtt.plugins.internal.v1.mapper.helper.SensorsMapperEmptyImpl;
import dev.cookiecode.rika2mqtt.rika.firenet.model.Controls;
import dev.cookiecode.rika2mqtt.rika.firenet.model.Sensors;
import dev.cookiecode.rika2mqtt.rika.firenet.model.StoveStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/** Test class */
@SpringBootTest(
    classes = {
      StoveStatusMapperImpl.class,
      SensorsMapperEmptyImpl.class,
      ControlsMapperEmptyImpl.class
    })
class StoveStatusMapperTest {

  @Autowired private StoveStatusMapper mapper;

  @Test
  void toApiStoveStatusShouldFillAllStoveProperties() {

    // GIVEN
    String name = "<name>";
    Long stoveId = 12L;
    Long lastConfirmedRevision = 12321312L;
    String oem = "<oem>";
    Long lastSeenMinutes = 0L;
    String stoveType = "<STOVE TYPE>";
    Sensors sensors = Sensors.builder().build();
    Controls controls = Controls.builder().build();
    StoveStatus rikaFirenetStatus =
        StoveStatus.builder()
            .name(name)
            .stoveId(stoveId)
            .lastConfirmedRevision(lastConfirmedRevision)
            .oem(oem)
            .lastSeenMinutes(lastSeenMinutes)
            .stoveType(stoveType)
            .sensors(sensors)
            .controls(controls)
            .build();

    // WHEN
    final var apiStatus = mapper.toApiStoveStatus(rikaFirenetStatus);

    // THEN
    assertThat(apiStatus).hasNoNullFieldsOrProperties();
  }
}
