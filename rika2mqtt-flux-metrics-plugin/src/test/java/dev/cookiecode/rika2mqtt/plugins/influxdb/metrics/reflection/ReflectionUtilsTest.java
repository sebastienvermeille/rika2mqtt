package dev.cookiecode.rika2mqtt.plugins.influxdb.metrics.reflection;

import org.junit.jupiter.api.Test;

import static dev.cookiecode.rika2mqtt.plugins.influxdb.metrics.reflection.ReflectionUtils.getPropertyGetterMethodName;
import static dev.cookiecode.rika2mqtt.plugins.influxdb.metrics.reflection.ReflectionUtils.isBooleanPrimitiveProperty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test class
 */
class ReflectionUtilsTest {

    @Test
    void isBooleanPropertyShouldReturnFalseGivenThePropertyIsABooleanObjectNotPrimitive(){

        // GIVEN
        final var clazz = TestedClass.class;
        final var booleanObjectPropertyName = "enabled";

        // WHEN
        final var result = isBooleanPrimitiveProperty(clazz, booleanObjectPropertyName);

        // THEN
        assertThat(result).isFalse();
    }

    @Test
    void isBooleanPropertyShouldReturnTrueGivenThePropertyIsABooleanPrimitive(){

        // GIVEN
        final var clazz = TestedClass.class;
        final var booleanPrimitivePropertyName = "disabled";

        // WHEN
        final var result = isBooleanPrimitiveProperty(clazz, booleanPrimitivePropertyName);

        // THEN
        assertThat(result).isTrue();
    }

    @Test
    void isBooleanPropertyShouldReturnFalseGivenThePropertyIsAString(){

        // GIVEN
        final var clazz = TestedClass.class;
        final var stringPropertyName = "name";

        // WHEN
        final var result = isBooleanPrimitiveProperty(clazz, stringPropertyName);

        // THEN
        assertThat(result).isFalse();
    }

    @Test
    void getPropertyGetterMethodNameShouldReturnAGetterStartingWithIsGivenAPrimitiveBooleanProperty(){

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
    void getPropertyGetterMethodNameShouldReturnAGetterStartingWithIsGivenAnObjectBooleanProperty(){

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
    void getPropertyGetterMethodNameShouldThrowAnExceptionGivenEmptyPropertyName(){
        // GIVEN
        final var clazz = TestedClass.class;
        final var emptyPropertyName = "";

        // WHEN/THEN
        assertThatThrownBy(() ->
           getPropertyGetterMethodName(clazz, emptyPropertyName)
        )
        .isInstanceOf(IllegalArgumentException.class);
    }
}
