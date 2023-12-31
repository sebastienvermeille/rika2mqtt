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
package dev.cookiecode.rika2mqtt.plugins.internal.v1.mapper.helper;

import dev.cookiecode.rika2mqtt.plugins.api.v1.model.Sensors;
import dev.cookiecode.rika2mqtt.plugins.internal.v1.mapper.SensorsMapper;
import lombok.NonNull;
import org.springframework.stereotype.Component;

/**
 * Test class This class is intentionally very empty (It allows to keep tests valuable and not
 * repeating themselves)
 *
 * @author Sebastien Vermeille
 */
@Component
public class SensorsMapperEmptyImpl implements SensorsMapper {

  @Override
  public Sensors toApiSensors(
      dev.cookiecode.rika2mqtt.rika.firenet.model.@NonNull Sensors sensors) {
    return Sensors.builder().build();
  }
}
