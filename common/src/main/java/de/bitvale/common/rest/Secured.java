package de.bitvale.common.rest;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author by Patrick Bittner on 12.06.15.
 */
@Target({METHOD, TYPE})
@Retention(RUNTIME)
@Documented
@NameBinding
public @interface Secured {}
