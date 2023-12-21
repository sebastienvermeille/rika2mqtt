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

import java.util.Optional;
import lombok.Getter;

@Getter
public class RequiredPluginConfigurationParameter {
  private final String parameterName;
  private final String description;
  private final Class<?> valueType;
  private String example;

  private RequiredPluginConfigurationParameter(Builder builder) {
    parameterName = builder.parameterName;
    description = builder.description;
    valueType = builder.valueType;
    example = builder.example;
  }

  public static IParameterName builder() {
    return new Builder();
  }

  public interface IBuild {
    IBuild withExample(String val);

    PluginConfigurationParameter build();
  }

  public interface IExample {
    IBuild withExample(String val);
  }

  public interface IValueType {
    IBuild withValueType(Class<?> val);
  }

  public interface IDescription {
    IValueType withDescription(String val);
  }

  public interface IParameterName {
    IDescription withParameterName(String val);
  }

  public static final class Builder
      implements IExample, IValueType, IDescription, IParameterName, IBuild {
    private String example;
    private Class<?> valueType;
    private String description;
    private String parameterName;

    Builder() {}

    @Override
    public IBuild withExample(String val) {
      example = val;
      return this;
    }

    @Override
    public IBuild withValueType(Class<?> val) {
      valueType = val;
      return this;
    }

    @Override
    public IValueType withDescription(String val) {
      description = val;
      return this;
    }

    @Override
    public IDescription withParameterName(String val) {
      parameterName = val;
      return this;
    }

    public PluginConfigurationParameter build() {
      return asPluginConfigurationParameter(new RequiredPluginConfigurationParameter(this));
    }
  }

  public Optional<String> getExample() {
    return Optional.ofNullable(example);
  }

  public static PluginConfigurationParameter asPluginConfigurationParameter(
      RequiredPluginConfigurationParameter requiredPluginConfigurationParameter) {
    return new PluginConfigurationParameter(requiredPluginConfigurationParameter);
  }
}
