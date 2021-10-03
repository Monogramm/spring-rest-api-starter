/*
 * Creation by madmath03 the 2017-11-17.
 */

package com.monogramm.starter.utils.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * The Custom Annotation for UUID Validation.
 * 
 * @see <a href=
 *      "https://stackoverflow.com/questions/37320870/is-there-a-uuid-validator-annotation">Is there
 *      a uuid validator annotation? </a>
 * 
 * @author madmath03
 */
@Target({FIELD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = UuidValidator.class)
@Documented
public @interface ValidUuid {

  /**
   * Error message when validation fails.
   * 
   * @return message when validation fails.
   */
  String message() default "Invalid UUID string";

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
