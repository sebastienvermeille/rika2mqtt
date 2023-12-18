package dev.cookiecode.rika2mqtt.plugins.influxdb.metrics.reflection;

public class ReflectionUtils {

  public static boolean isBooleanProperty(Class<?> clazz, String propertyName) {
    String booleanGetterMethodName =
        "is" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    try {
      clazz.getMethod(booleanGetterMethodName);
      return true;
    } catch (NoSuchMethodException e) {
      return false;
    }
  }
}
