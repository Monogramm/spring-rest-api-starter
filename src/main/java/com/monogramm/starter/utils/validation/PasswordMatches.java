/*
 * Creation by madmath03 the 2017-11-05.
 */

package com.monogramm.starter.utils.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * The Custom Annotation for Validating a Password Confirmation.
 * 
 * @see <a href="http://www.baeldung.com/registration-with-spring-mvc-and-spring-security">Baeldung:
 *      The Registration Process With Spring Security</a>
 * 
 * @author madmath03
 */
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented
public @interface PasswordMatches {

  /**
   * Error message when validation fails.
   * 
   * @return message when validation fails.
   */
  String message() default "Passwords don't match";

  /**
   * The validation groups.
   * 
   * @return the validation groups.
   */
  Class<?>[] groups() default {};

  /**
   * Custom payload.
   * 
   * @return custom payload.
   */
  Class<? extends Payload>[] payload() default {};
}
