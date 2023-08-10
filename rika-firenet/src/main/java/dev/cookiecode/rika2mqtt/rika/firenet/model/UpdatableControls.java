/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package dev.cookiecode.rika2mqtt.rika.firenet.model;

import dev.cookiecode.rika2mqtt.rika.firenet.RikaFirenetServiceImpl;
import dev.cookiecode.rika2mqtt.rika.firenet.mapper.UpdatableControlsMapper;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.util.Map;

/**
 * @author Sebastien Vermeille
 * @implNote Each change in this field impacts directly {@link UpdatableControlsMapper}.
 */
@Data
@FieldNameConstants(level = AccessLevel.PUBLIC)
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
}
