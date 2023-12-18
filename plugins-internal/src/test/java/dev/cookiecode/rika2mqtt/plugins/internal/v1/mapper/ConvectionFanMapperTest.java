package dev.cookiecode.rika2mqtt.plugins.internal.v1.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/** Test class */
@SpringBootTest(classes = {ConvectionFanMapperImpl.class})
class ConvectionFanMapperTest {

  @Autowired private ConvectionFanMapper mapper;

  @Test
  void mapShouldConvertSuccessfullyGivenFanProperties() {

    // GIVEN
    final var identifier = 1;
    final var active = true;
    final var level = 12;
    final var area = 50;

    // WHEN
    final var result = mapper.map(identifier, active, level, area);

    // THEN
    assertThat(result.getIdentifier()).isEqualTo(identifier);
    assertThat(result.isActive()).isEqualTo(active);
    assertThat(result.getLevel()).isEqualTo(level);
    assertThat(result.getArea()).isEqualTo(area);
  }
}
