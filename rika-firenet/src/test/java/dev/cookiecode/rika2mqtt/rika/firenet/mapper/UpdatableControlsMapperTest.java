/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package dev.cookiecode.rika2mqtt.rika.firenet.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import dev.cookiecode.rika2mqtt.rika.firenet.model.UpdatableControls;
import dev.cookiecode.rika2mqtt.rika.firenet.model.UpdatableControls.Fields;
import java.util.HashMap;
import lombok.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test class
 */
@SpringBootTest(classes = {UpdatableControlsMapperImpl.class})
@ActiveProfiles("test")
class UpdatableControlsMapperTest {

  @Autowired
  private UpdatableControlsMapper mapper;

  @Test
  void test() {

    // GIVEN
    final Long expectedRevision = 323131L;
    final Integer expectedTargetTemperature = 18;

    var props = new HashMap<String, String>();
    props.put(Fields.REVISION, expectedRevision.toString());
    props.put(Fields.TARGET_TEMPERATURE, expectedTargetTemperature.toString());

    // WHEN
    var actualDto = mapper.toUpdateControls(props);

    // THEN
    assertThat(actualDto.getRevision()).isEqualTo(expectedRevision);
    assertThat(actualDto.getTargetTemperature()).isEqualTo(expectedTargetTemperature);

  }

  @Test
  void taaata() {
    final Long expectedRevision = 323131L;
    @NonNull UpdatableControls updatableControls = UpdatableControls.builder()
        .revision(expectedRevision)
        .targetTemperature(12)
        .build();
    var map = mapper.toMap(updatableControls);

    assertThat(map.get("revision")).isEqualTo(expectedRevision);

  }
}