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
package dev.cookiecode.rika2mqtt.rika.firenet.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

/**
 * @author Sebastien Vermeille
 */
@Data
@Builder
public class Controls {

  private Long revision;
  private Boolean onOff;
  private Integer operatingMode;
  private Integer heatingPower;
  private Integer targetTemperature;
  private Integer bakeTemperature;
  private Boolean ecoMode;

  // Heating time format: heatingTimeWed2: "13302200" (13:30 - 22:00)
  private String heatingTimeMon1;
  private String heatingTimeMon2;
  private String heatingTimeTue1;
  private String heatingTimeTue2;
  private String heatingTimeWed1;
  private String heatingTimeWed2;
  private String heatingTimeThu1;
  private String heatingTimeThu2;
  private String heatingTimeFri1;
  private String heatingTimeFri2;
  private String heatingTimeSat1;
  private String heatingTimeSat2;
  private String heatingTimeSun1;
  private String heatingTimeSun2;

  private Boolean heatingTimesActiveForComfort;
  private Integer setBackTemperature;
  private Boolean convectionFan1Active;
  private Integer convectionFan1Level;
  private Integer convectionFan1Area;
  private Boolean convectionFan2Active;
  private Integer convectionFan2Level;
  private Integer convectionFan2Area;
  private Boolean frostProtectionActive;
  private Integer frostProtectionTemperature;
  private Double temperatureOffset;

  @SerializedName("RoomPowerRequest") // for coherence (the rest of the api is using camelCase)
  private Integer roomPowerRequest;

  private Integer debug0;
  private Integer debug1;
  private Integer debug2;
  private Integer debug3;
  private Integer debug4;
}
