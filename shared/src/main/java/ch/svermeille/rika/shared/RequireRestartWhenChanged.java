package ch.svermeille.rika.shared;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Sebastien Vermeille
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface RequireRestartWhenChanged {
  boolean value() default true;

  String propertyPath() default "";
}
