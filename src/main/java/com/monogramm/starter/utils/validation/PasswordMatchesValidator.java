/*
 * Creation by madmath03 the 2017-11-05.
 */

package com.monogramm.starter.utils.validation;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * The PasswordMatchesValidator Custom Validator.
 * 
 * @see <a href="http://www.baeldung.com/registration-with-spring-mvc-and-spring-security">Baeldung:
 *      The Registration Process With Spring Security</a>
 * 
 * @author madmath03
 */
public class PasswordMatchesValidator
    implements ConstraintValidator<PasswordMatches, PasswordConfirmationDto> {

  @Override
  public void initialize(final PasswordMatches constraintAnnotation) {
    // Nothing to be initialized
  }

  @Override
  public boolean isValid(final PasswordConfirmationDto dto,
      final ConstraintValidatorContext context) {
    final boolean valid;

    if (dto == null) {
      valid = false;
    } else {
      valid = Arrays.equals(dto.getPassword(), dto.getMatchingPassword());
    }

    return valid;
  }
}
