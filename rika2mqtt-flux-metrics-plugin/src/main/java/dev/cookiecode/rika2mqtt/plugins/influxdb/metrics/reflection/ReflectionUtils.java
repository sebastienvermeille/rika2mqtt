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

import java.lang.reflect.Method;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ReflectionUtils {

  /**
   * @param clazz the class owning the property
   * @param propertyName the property name
   * @return true when the property of the class is a primitive boolean type
   */
  public static boolean isBooleanPrimitiveProperty(
      @NonNull Class<?> clazz, @NonNull String propertyName) {
    final var booleanGetterMethodName =
        "is" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    try {
      // TODO: exception driven code... code smell (more elegant solution should be privileged)
      clazz.getMethod(booleanGetterMethodName);
      return true;
    } catch (NoSuchMethodException e) {
      return false;
    }
  }

  public static String getPropertyGetterMethodName(
      @NonNull Class<?> clazz, @NonNull String propertyName) {
    if (propertyName.isEmpty()) {
      throw new IllegalArgumentException(
          "propertyName is empty. Please provide a valid propertyName.");
    }
    String getterMethodName;

    if (isBooleanPrimitiveProperty(clazz, propertyName)) {
      getterMethodName =
          "is" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    } else {
      getterMethodName =
          "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }

    return getterMethodName;
  }

  public static Method getPropertyGetterMethod(
      @NonNull Class<?> clazz, @NonNull String propertyName) throws NoSuchMethodException {
    final var propertyGetterMethodName = getPropertyGetterMethodName(clazz, propertyName);

    return clazz.getMethod(propertyGetterMethodName);
  }
}
