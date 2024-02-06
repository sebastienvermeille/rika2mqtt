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
package dev.cookiecode.rika2mqtt.plugins.internal.v1.event;

import dev.cookiecode.rika2mqtt.plugins.api.Beta;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.StoveError;
import lombok.*;

/**
 * Triggered when RIKA stove is printing an error
 *
 * @author Sebastien Vermeille
 */
@Getter
@ToString
@EqualsAndHashCode
@Beta
public class StoveErrorEvent implements Rika2MqttPluginEvent {
  private final StoveError stoveError;

  private StoveErrorEvent(Builder builder) {
    stoveError = builder.stoveError;
  }

  public static IStoveError builder() {
    return new Builder();
  }

  public interface IBuild {
    StoveErrorEvent build();
  }

  public interface IStoveError {
    IBuild withStoveError(StoveError val);
  }

  /** {@code StoveErrorEvent} builder static inner class. */
  public static final class Builder implements IStoveError, IBuild {
    private StoveError stoveError;

    private Builder() {}

    /**
     * Sets the {@code stoveError} and returns a reference to {@code IBuild}
     *
     * @param val the {@code stoveError} to set
     * @return a reference to this Builder
     */
    @Override
    public IBuild withStoveError(StoveError val) {
      stoveError = val;
      return this;
    }

    /**
     * Returns a {@code StoveErrorEvent} built from the parameters previously set.
     *
     * @return a {@code StoveErrorEvent} built with parameters of this {@code
     *     StoveErrorEvent.Builder}
     */
    public StoveErrorEvent build() {
      return new StoveErrorEvent(this);
    }
  }
}
