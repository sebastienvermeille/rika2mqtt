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

import static java.lang.Integer.parseInt;
import static org.mapstruct.ReportingPolicy.ERROR;

import dev.cookiecode.rika2mqtt.plugins.api.Beta;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.TimeDefinition;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.TimeRange;
import lombok.NonNull;
import org.mapstruct.Mapper;

/**
 * @author Sebastien Vermeille
 */
@Beta
@Mapper(unmappedTargetPolicy = ERROR)
public interface TimeRangeMapper {
  /**
   * @param rikaTimeRange i.e: `13302200`
   * @return a TimeRange from 13:30 to 22:00
   */
  default TimeRange map(@NonNull String rikaTimeRange) {

    var from = rikaTimeRange.substring(0, 4);
    var to = rikaTimeRange.substring(4);
    return TimeRange.builder()
        .from(
            TimeDefinition.builder()
                .hours(parseInt(from.substring(0, 2)))
                .minutes(parseInt(from.substring(2)))
                .build())
        .to(
            TimeDefinition.builder()
                .hours(parseInt(to.substring(0, 2)))
                .minutes(parseInt(to.substring(2)))
                .build())
        .build();
  }
}
