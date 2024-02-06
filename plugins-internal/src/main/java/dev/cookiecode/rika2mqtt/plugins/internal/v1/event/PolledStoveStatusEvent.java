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
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.StoveStatus;
import lombok.*;

/**
 * @author Sebastien Vermeille
 */
@Getter
@ToString
@EqualsAndHashCode
@Beta
public class PolledStoveStatusEvent implements Rika2MqttPluginEvent {
  private final StoveStatus stoveStatus;

  private PolledStoveStatusEvent(Builder builder) {
    stoveStatus = builder.stoveStatus;
  }

  public static IStoveStatus builder() {
    return new Builder();
  }

  public interface IBuild {
    PolledStoveStatusEvent build();
  }

  public interface IStoveStatus {
    IBuild withStoveStatus(StoveStatus val);
  }

  /** {@code PolledStoveStatusEvent} builder static inner class. */
  public static final class Builder implements IStoveStatus, IBuild {
    private StoveStatus stoveStatus;

    private Builder() {}

    /**
     * Sets the {@code stoveStatus} and returns a reference to {@code IBuild}
     *
     * @param val the {@code stoveStatus} to set
     * @return a reference to this Builder
     */
    @Override
    public IBuild withStoveStatus(StoveStatus val) {
      stoveStatus = val;
      return this;
    }

    /**
     * Returns a {@code PolledStoveStatusEvent} built from the parameters previously set.
     *
     * @return a {@code PolledStoveStatusEvent} built with parameters of this {@code
     *     PolledStoveStatusEvent.Builder}
     */
    public PolledStoveStatusEvent build() {
      return new PolledStoveStatusEvent(this);
    }
  }
}
