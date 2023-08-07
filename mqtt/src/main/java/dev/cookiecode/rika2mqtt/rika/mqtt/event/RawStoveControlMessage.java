/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package dev.cookiecode.rika2mqtt.rika.mqtt.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Builder
@AllArgsConstructor
@Getter
@RequiredArgsConstructor
@ToString
public class RawStoveControlMessage {

  private final Long stoveID; // added by rika2mqtt

  private final int operatingMode;
  private final int heatingPower;
  private final int targetTemperature;
  private final int bakeTemperature;
  private final boolean onOff;
  private final boolean heatingTimesActiveForComfort;
  private final int setBackTemperature;
  private final boolean convectionFan1Active;
  private final int convectionFan1Level;
  private final int convectionFan1Area;
  private final boolean convectionFan2Active;
  private final int convectionFan2Level;
  private final int convectionFan2Area;
  private final boolean frostProtectionActive;
  private final int frostProtectionTemperature;
  private long revision; // 1684317751 (unix timestamp)
}
