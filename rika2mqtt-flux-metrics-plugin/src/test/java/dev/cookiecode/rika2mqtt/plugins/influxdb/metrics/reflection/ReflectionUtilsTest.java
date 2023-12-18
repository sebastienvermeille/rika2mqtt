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
package dev.cookiecode.rika2mqtt.plugins.influxdb.metrics.reflection;

import static dev.cookiecode.rika2mqtt.plugins.influxdb.metrics.reflection.ReflectionUtils.getPropertyGetterMethodName;
import static dev.cookiecode.rika2mqtt.plugins.influxdb.metrics.reflection.ReflectionUtils.isBooleanPrimitiveProperty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

/** Test class */
class ReflectionUtilsTest {

  @Test
  void isBooleanPropertyShouldReturnFalseGivenThePropertyIsABooleanObjectNotPrimitive() {

    // GIVEN
    final var clazz = TestedClass.class;
    final var booleanObjectPropertyName = "enabled";

    // WHEN
    final var result = isBooleanPrimitiveProperty(clazz, booleanObjectPropertyName);

    // THEN
    assertThat(result).isFalse();
  }

  @Test
  void isBooleanPropertyShouldReturnTrueGivenThePropertyIsABooleanPrimitive() {

    // GIVEN
    final var clazz = TestedClass.class;
    final var booleanPrimitivePropertyName = "disabled";

    // WHEN
    final var result = isBooleanPrimitiveProperty(clazz, booleanPrimitivePropertyName);

    // THEN
    assertThat(result).isTrue();
  }

  @Test
  void isBooleanPropertyShouldReturnFalseGivenThePropertyIsAString() {

    // GIVEN
    final var clazz = TestedClass.class;
    final var stringPropertyName = "name";

    // WHEN
    final var result = isBooleanPrimitiveProperty(clazz, stringPropertyName);

    // THEN
    assertThat(result).isFalse();
  }

  @Test
  void
      getPropertyGetterMethodNameShouldReturnAGetterStartingWithIsGivenAPrimitiveBooleanProperty() {

    // GIVEN
    final var clazz = TestedClass.class;
    final var primitiveBooleanPropertyName = "disabled";

    // WHEN
    final var result = getPropertyGetterMethodName(clazz, primitiveBooleanPropertyName);

    // THEN
    assertThat(result).startsWith("is");
    assertThat(result).isEqualTo("isDisabled");
  }

  @Test
  void getPropertyGetterMethodNameShouldReturnAGetterStartingWithIsGivenAnObjectBooleanProperty() {

    // GIVEN
    final var clazz = TestedClass.class;
    final var objectBooleanPropertyName = "enabled";

    // WHEN
    final var result = getPropertyGetterMethodName(clazz, objectBooleanPropertyName);

    // THEN
    assertThat(result).startsWith("get");
    assertThat(result).isEqualTo("getEnabled");
  }

  @Test
  void getPropertyGetterMethodNameShouldThrowAnExceptionGivenEmptyPropertyName() {
    // GIVEN
    final var clazz = TestedClass.class;
    final var emptyPropertyName = "";

    // WHEN/THEN
    assertThatThrownBy(() -> getPropertyGetterMethodName(clazz, emptyPropertyName))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
