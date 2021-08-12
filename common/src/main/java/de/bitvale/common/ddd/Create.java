package de.bitvale.common.ddd;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Patrick Bittner on 08/04/15.
 */
@Target({PARAMETER, FIELD})
@Retention(RUNTIME)
@Documented
@Qualifier
public @interface Create {
}
