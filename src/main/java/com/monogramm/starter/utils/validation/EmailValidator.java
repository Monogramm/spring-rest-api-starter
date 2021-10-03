/*
 * Creation by madmath03 the 2017-11-05.
 */

package com.monogramm.starter.utils.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * The Custom EmailValidator.
 * 
 * @see <a href="http://www.baeldung.com/registration-with-spring-mvc-and-spring-security">Baeldung:
 *      The Registration Process With Spring Security</a>
 * 
 * @author madmath03
 */
public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
  private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
      + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
  private static final Pattern PATTERN = Pattern.compile(EMAIL_PATTERN);

  private Matcher matcher;

  @Override
  public void initialize(final ValidEmail constraintAnnotation) {
    this.matcher = null;
  }

  @Override
  public boolean isValid(final String email, final ConstraintValidatorContext context) {
    return validateEmail(email);
  }

  private boolean validateEmail(final String email) {
    final boolean valid;

    if (email == null) {
      valid = false;
    } else {
      this.matcher = PATTERN.matcher(email);
      valid = this.matcher.matches();
    }

    return valid;
  }
}
