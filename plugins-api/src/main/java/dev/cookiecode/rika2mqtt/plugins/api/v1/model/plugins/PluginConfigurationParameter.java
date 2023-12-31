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
import java.util.Optional;
import javax.annotation.processing.Generated;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author Sebastien Vermeille
 */
@Generated(
    value = "Step Builder generator Idea Plugin",
    date = "2023-12-31T09:40:55+0100",
    comments = "parsely adapted by hands to handle optional props")
@Getter
@ToString
@EqualsAndHashCode
@Beta
public class PluginConfigurationParameter {

  private final String parameterName;
  private final String description;
  private final Class<?> valueType;
  private final String example;
  private final boolean required;
  private final Object defaultValue;

  protected PluginConfigurationParameter(
      @NonNull OptionalPluginConfigurationParameter optionalParameter) {
    required = false;
    parameterName = optionalParameter.getParameterName();
    description = optionalParameter.getDescription();
    valueType = optionalParameter.getValueType();
    example = optionalParameter.getExample().orElse(null);
    defaultValue = optionalParameter.getDefaultValue().orElse(null);

    checkCoherence();
  }

  public PluginConfigurationParameter(
      @NonNull RequiredPluginConfigurationParameter requiredParameter) {
    required = true;
    parameterName = requiredParameter.getParameterName();
    description = requiredParameter.getDescription();
    valueType = requiredParameter.getValueType();
    example = requiredParameter.getExample().orElse(null);
    defaultValue = null;
  }

  private void checkCoherence() {
    if (this.required && this.defaultValue != null) {
      throw new IllegalArgumentException(
          String.format(
              """
            Dear developer, please take position.
            Either '%s' is optional and have a default value,
            either it is required and the user must provide a value for it.
            """,
              this.parameterName));
    }
  }

  public Optional<Object> getDefaultValue() {
    return Optional.ofNullable(defaultValue);
  }

  public Optional<String> getExample() {
    return Optional.ofNullable(example);
  }

  public boolean isOptional() {
    return !isRequired();
  }

  public static RequiredPluginConfigurationParameter.Builder newRequiredParameter() {
    return new RequiredPluginConfigurationParameter.Builder();
  }
}
