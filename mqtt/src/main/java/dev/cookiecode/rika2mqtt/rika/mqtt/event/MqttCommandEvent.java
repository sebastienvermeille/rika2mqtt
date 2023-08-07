/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package dev.cookiecode.rika2mqtt.rika.mqtt.event;

import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @author Sebastien Vermeille
 */
@ToString
@RequiredArgsConstructor
public class MqttCommandEvent {

  @Getter
  private final Long stoveId;

  @Getter
  private final Map<String, String> props;

  @Getter
  private final RawStoveControlMessage message;
}
