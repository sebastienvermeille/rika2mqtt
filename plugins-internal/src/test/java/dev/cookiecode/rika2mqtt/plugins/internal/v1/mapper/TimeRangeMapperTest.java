package dev.cookiecode.rika2mqtt.plugins.internal.v1.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/** Test class */
@SpringBootTest(classes = {TimeRangeMapperImpl.class})
class TimeRangeMapperTest {

  @Autowired private TimeRangeMapper mapper;

  @Test
  void mapShouldConvertSuccessfullyGivenAValidRikaTimeRangeInputGivenDoubleDigitTimes() {

    // GIVEN
    final var rikaTimeRange = "13302215";

    // WHEN
    final var result = mapper.map(rikaTimeRange);

    // THEN
    assertThat(result.toString()).isEqualTo("13:30 - 22:15");
  }

  @Test
  void mapShouldConvertSuccessfullyGivenAValidRikaTimeRangeInputGivenSingleDigitTimes() {

    // GIVEN
    final var rikaTimeRange = "13012205";

    // WHEN
    final var result = mapper.map(rikaTimeRange);

    // THEN
    assertThat(result.toString()).isEqualTo("13:01 - 22:05");
  }
}
