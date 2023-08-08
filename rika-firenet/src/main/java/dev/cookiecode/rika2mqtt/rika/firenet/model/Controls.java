/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
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
  private Integer temperatureOffset;

  @SerializedName("RoomPowerRequest") // for coherence (the rest of the api is using camelCase)
  private Integer roomPowerRequest;
  private Integer debug0;
  private Integer debug1;
  private Integer debug2;
  private Integer debug3;
  private Integer debug4;
}
