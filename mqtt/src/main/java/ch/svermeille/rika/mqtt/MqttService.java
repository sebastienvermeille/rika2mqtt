/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.svermeille.rika.mqtt;

/**
 * @author Sebastien Vermeille
 */
public interface MqttService {

  void publish(String message);

}
