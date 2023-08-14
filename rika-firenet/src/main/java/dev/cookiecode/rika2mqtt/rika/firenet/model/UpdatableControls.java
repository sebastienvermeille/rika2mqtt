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

import static lombok.AccessLevel.NONE;

import dev.cookiecode.rika2mqtt.rika.firenet.RikaFirenetServiceImpl;
import dev.cookiecode.rika2mqtt.rika.firenet.mapper.UpdatableControlsMapper;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sebastien Vermeille
 * @implNote Each change in this field impacts directly {@link UpdatableControlsMapper}.
 */
@Data
public class UpdatableControls {
  /**
   * revision field is managed by rika2mqtt {@link RikaFirenetServiceImpl#overrideRevision(Map,
   * UpdatableControls)}
   */
  private Long revision;
  /** Fields below (some, or all) can be sent using MQTT command to pilot RIKA. */
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

  // @FieldNameConstants lombok annotation would generate this. Unfortunately at the moment it
  // generates issues to generate
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
