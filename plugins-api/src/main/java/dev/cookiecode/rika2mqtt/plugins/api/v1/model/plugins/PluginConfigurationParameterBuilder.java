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
package dev.cookiecode.rika2mqtt.plugins.api.v1.model.plugins;

import dev.cookiecode.rika2mqtt.plugins.api.Beta;
import javax.annotation.processing.Generated;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Generated(
    value = "Step Builder generator Idea Plugin",
    date = "2023-12-31T09:40:55+0100",
    comments = "parsely adapted by hands to handle optional props")
@Getter
@ToString
@EqualsAndHashCode
@Beta
public class PluginConfigurationParameterBuilder {

  private final boolean required;

  PluginConfigurationParameterBuilder(Builder builder) {
    required = builder.required;
  }

  public static IRequired builder() {
    return new Builder();
  }

  public interface IBuild {
    PluginConfigurationParameterBuilder build();
  }

  public interface IRequired {
    IBuild withRequired(boolean val);
  }

  public static final class Builder implements IRequired, IBuild {
    private boolean required;

    private Builder() {}

    @Override
    public IBuild withRequired(boolean val) {
      required = val;
      return this;
    }

    public PluginConfigurationParameterBuilder build() {
      return new PluginConfigurationParameterBuilder(this);
    }
  }
}
