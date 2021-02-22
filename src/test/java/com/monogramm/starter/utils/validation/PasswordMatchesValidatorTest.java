/*
 * Creation by madmath03 the 2017-11-10.
 */

package com.monogramm.starter.utils.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import javax.validation.ConstraintValidatorContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * {@link PasswordMatchesValidator} Unit Test.
 * 
 * @author madmath03
 */
public class PasswordMatchesValidatorTest {
  private static final char[] PASSWORD = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
  private static final char[] DROWSSAP = {'d', 'r', 'o', 'w', 's', 's', 'a', 'p'};

  private PasswordMatches constraintAnnotation;
  private ConstraintValidatorContext context;

  private PasswordMatchesValidator validator;

  /**
   * @throws java.lang.Exception if the test setup crashes.
   */
  @Before
  public void setUp() throws Exception {
    this.constraintAnnotation = mock(PasswordMatches.class);
    this.context = mock(ConstraintValidatorContext.class);

    this.validator = new PasswordMatchesValidator();
  }

  /**
   * @throws java.lang.Exception if the test cleanup crashes.
   */
  @After
  public void tearDown() throws Exception {
    Mockito.reset(context);

    this.validator = null;
  }

  /**
   * Test method for {@link PasswordMatchesValidator#initialize(PasswordMatches)}.
   */
  @Test
  public void testInitialize() {
    this.validator.initialize(this.constraintAnnotation);
    // Nothing to test
  }

  /**
   * Test method for
   * {@link PasswordMatchesValidator#isValid(PasswordConfirmationDto, ConstraintValidatorContext)}.
   */
  @Test
  public void testIsValid() {
    assertFalse(this.validator.isValid(null, this.context));

    assertFalse(
        this.validator.isValid(new PasswordConfirmationDto(PASSWORD, DROWSSAP), this.context));

    assertFalse(
        this.validator.isValid(new PasswordConfirmationDto(DROWSSAP, PASSWORD), this.context));

    assertTrue(
        this.validator.isValid(new PasswordConfirmationDto(PASSWORD, PASSWORD), this.context));

    assertTrue(
        this.validator.isValid(new PasswordConfirmationDto(DROWSSAP, DROWSSAP), this.context));
  }

}
