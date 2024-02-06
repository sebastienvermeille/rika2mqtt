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
package dev.cookiecode.rika2mqtt.rika.mqtt.event;

import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Sebastien Vermeille
 */
@Getter
@EqualsAndHashCode
@ToString
public class MqttCommandEvent {

  private final Long stoveId;

  private final Map<String, String> props;

  private MqttCommandEvent(Builder builder) {
    stoveId = builder.stoveId;
    props = builder.props;
  }

  public static IStoveId builder() {
    return new Builder();
  }

  public interface IBuild {
    MqttCommandEvent build();
  }

  public interface IProps {
    IBuild withProps(Map<String, String> val);
  }

  public interface IStoveId {
    IProps withStoveId(Long val);
  }

  /** {@code MqttCommandEvent} builder static inner class. */
  public static final class Builder implements IProps, IStoveId, IBuild {
    private Map<String, String> props;
    private Long stoveId;

    private Builder() {}

    /**
     * Sets the {@code props} and returns a reference to {@code IBuild}
     *
     * @param val the {@code props} to set
     * @return a reference to this Builder
     */
    @Override
    public IBuild withProps(Map<String, String> val) {
      props = val;
      return this;
    }

    /**
     * Sets the {@code stoveId} and returns a reference to {@code IProps}
     *
     * @param val the {@code stoveId} to set
     * @return a reference to this Builder
     */
    @Override
    public IProps withStoveId(Long val) {
      stoveId = val;
      return this;
    }

    /**
     * Returns a {@code MqttCommandEvent} built from the parameters previously set.
     *
     * @return a {@code MqttCommandEvent} built with parameters of this {@code
     *     MqttCommandEvent.Builder}
     */
    public MqttCommandEvent build() {
      return new MqttCommandEvent(this);
    }
  }
}
