/*
 * Creation by madmath03 the 2017-11-17.
 */

package com.monogramm.starter.utils.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * The Custom UuidValidator.
 * 
 * @see <a href=
 *      "https://stackoverflow.com/questions/37320870/is-there-a-uuid-validator-annotation">Is there
 *      a uuid validator annotation? </a>
 * 
 * @author madmath03
 */
public class UuidValidator implements ConstraintValidator<ValidUuid, String> {
  private static final String UUID_PATTERN =
      "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";
  private static final Pattern PATTERN = Pattern.compile(UUID_PATTERN, Pattern.CASE_INSENSITIVE);

  private Matcher matcher;

  @Override
  public void initialize(final ValidUuid constraintAnnotation) {
    this.matcher = null;
  }

  @Override
  public boolean isValid(final String uuid, final ConstraintValidatorContext context) {
    return validateUuid(uuid);
  }

  private boolean validateUuid(final String uuid) {
    final boolean valid;

    if (uuid == null) {
      valid = false;
    } else {
      this.matcher = PATTERN.matcher(uuid);
      valid = this.matcher.matches();
    }

    return valid;
  }
}
