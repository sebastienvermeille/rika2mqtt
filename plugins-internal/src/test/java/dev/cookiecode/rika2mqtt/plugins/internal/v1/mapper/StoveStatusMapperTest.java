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
