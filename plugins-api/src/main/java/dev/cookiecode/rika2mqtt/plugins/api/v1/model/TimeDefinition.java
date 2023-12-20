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
package dev.cookiecode.rika2mqtt.plugins.api.v1.model;

import dev.cookiecode.rika2mqtt.plugins.api.Beta;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/** Class to represent time schedules such as : 10:00 - 12:30 */
@Builder
@Getter
@EqualsAndHashCode
@Beta
public class TimeDefinition {
  private final int hours;
  private final int minutes;

  private static final Double ONE_MINUTE_DURATION_IN_SECONDS = 60.0;

  @Override
  public String toString() {
    return String.format("%s:%02d", hours, minutes);
  }

  /** Helper method to return 9.5 given 9h30 */
  public Double asDecimal() {
    return this.getHours() + (this.getMinutes() / ONE_MINUTE_DURATION_IN_SECONDS);
  }
}
