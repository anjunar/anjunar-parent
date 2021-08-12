package de.bitvale.common.rest.api.meta;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, METHOD})
@Retention(RUNTIME)
@Documented
public @interface Input {

    String type() default "";

    boolean ignore() default false;

    boolean naming() default false;

    boolean primaryKey() default false;

}
