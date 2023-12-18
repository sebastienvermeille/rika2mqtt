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

import static lombok.AccessLevel.NONE;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/**
 * @author Sebastien Vermeille
 */
@Data
@Builder
public class Controls {

  private long revision;
  private boolean on;
  private int operatingMode;
  private int heatingPower;
  private int targetTemperature;
  private int bakeTemperature;
  private boolean ecoModeEnabled;

  // region HeatingTime
  // TODO: consider removing these properties handled in getHeatingTimes()
  @Getter(NONE)
  private TimeRange heatingTimeMon1;

  @Getter(NONE)
  private TimeRange heatingTimeMon2;

  @Getter(NONE)
  private TimeRange heatingTimeTue1;

  @Getter(NONE)
  private TimeRange heatingTimeTue2;

  @Getter(NONE)
  private TimeRange heatingTimeWed1;

  @Getter(NONE)
  private TimeRange heatingTimeWed2;

  @Getter(NONE)
  private TimeRange heatingTimeThu1;

  @Getter(NONE)
  private TimeRange heatingTimeThu2;

  @Getter(NONE)
  private TimeRange heatingTimeFri1;

  @Getter(NONE)
  private TimeRange heatingTimeFri2;

  @Getter(NONE)
  private TimeRange heatingTimeSat1;

  @Getter(NONE)
  private TimeRange heatingTimeSat2;

  @Getter(NONE)
  private TimeRange heatingTimeSun1;

  @Getter(NONE)
  private TimeRange heatingTimeSun2;

  // endregion
  private Map<DayOfWeek, List<TimeRange>> heatingTimes;

  private Boolean heatingTimesActiveForComfort;
  private Integer setBackTemperature;

  // region Fan
  // TODO: consider removing these properties handled in getFans()
  @Getter(NONE)
  private boolean convectionFan1Active;

  @Getter(NONE)
  private int convectionFan1Level;

  @Getter(NONE)
  private int convectionFan1Area;

  @Getter(NONE)
  private boolean convectionFan2Active;

  @Getter(NONE)
  private int convectionFan2Level;

  @Getter(NONE)
  private int convectionFan2Area;

  // endregion
  private List<ConvectionFan> fans;

  private boolean frostProtectionActive;
  private int frostProtectionTemperature;
  private double temperatureOffset;

  //  @SerializedName("RoomPowerRequest") // for coherence (the rest of the api is using camelCase)
  private int roomPowerRequest;

  // region Debug
  // TODO: consider removing these properties handled in getDebugs()
  @Getter(NONE)
  private int debug0;

  @Getter(NONE)
  private int debug1;

  @Getter(NONE)
  private int debug2;

  @Getter(NONE)
  private int debug3;

  @Getter(NONE)
  private int debug4;

  // endregion
  private List<Integer> debugs;
}
