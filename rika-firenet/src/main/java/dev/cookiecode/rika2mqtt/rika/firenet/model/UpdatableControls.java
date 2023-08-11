/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package dev.cookiecode.rika2mqtt.rika.firenet.model;

import static lombok.AccessLevel.NONE;

import dev.cookiecode.rika2mqtt.rika.firenet.RikaFirenetServiceImpl;
import dev.cookiecode.rika2mqtt.rika.firenet.mapper.UpdatableControlsMapper;
import lombok.AccessLevel;
import lombok.Data;

import java.util.Map;
import lombok.NoArgsConstructor;

/**
 * @author Sebastien Vermeille
 * @implNote Each change in this field impacts directly {@link UpdatableControlsMapper}.
 */
@Data
public class UpdatableControls {
  /**
   * revision field is managed by rika2mqtt {@link RikaFirenetServiceImpl#overrideRevision(Map, UpdatableControls)}
   */
  private Long revision;
  /**
   * Fields below (some, or all) can be sent using MQTT command to pilot RIKA.
   */
  private Integer operatingMode;
  private Integer heatingPower;
  private Integer targetTemperature;
  private Integer bakeTemperature;
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
  private Boolean onOff;

  // @FieldNameConstants lombok annotation would generate this. Unfortunately at the moment it generates issues to generate
  // javadoc: error: cannot find symbol
  @NoArgsConstructor(access = NONE)
  public static final class Fields {
    public static final String REVISION = "revision";
    public static final String OPERATING_MODE = "operatingMode";
    public static final String HEATING_POWER = "heatingPower";
    public static final String TARGET_TEMPERATURE = "targetTemperature";
    public static final String BAKE_TEMPERATURE = "bakeTemperature";
    public static final String HEATING_TIMES_ACTIVE_FOR_COMFORT = "heatingTimesActiveForComfort";
    public static final String SET_BACK_TEMPERATURE = "setBackTemperature";
    public static final String CONVECTION_FAN1_ACTIVE = "convectionFan1Active";
    public static final String CONVECTION_FAN1_LEVEL = "convectionFan1Level";
    public static final String CONVECTION_FAN1_AREA = "convectionFan1Area";
    public static final String CONVECTION_FAN2_ACTIVE = "convectionFan2Active";
    public static final String CONVECTION_FAN2_LEVEL = "convectionFan2Level";
    public static final String CONVECTION_FAN2_AREA = "convectionFan2Area";
    public static final String FROST_PROTECTION_ACTIVE = "frostProtectionActive";
    public static final String FROST_PROTECTION_TEMPERATURE = "frostProtectionTemperature";
    public static final String ON_OFF = "onOff";
  }
}
