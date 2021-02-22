/*
 * Creation by madmath03 the 2017-11-17.
 */

package com.monogramm.starter.utils.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.UUID;

import javax.validation.ConstraintValidatorContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * {@link UuidValidator} Unit Test.
 * 
 * @author madmath03
 */
public class UuidValidatorTest {

  private ValidUuid constraintAnnotation;

  private ConstraintValidatorContext context;

  private UuidValidator validator;

  /**
   * @throws java.lang.Exception if the test setup crashes.
   */
  @Before
  public void setUp() throws Exception {
    this.context = mock(ConstraintValidatorContext.class);

    this.validator = new UuidValidator();
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
   * Test method for {@link UuidValidator#initialize(ValidUuid)}.
   */
  @Test
  public void testInitialize() {
    this.validator.initialize(this.constraintAnnotation);
  }

  /**
   * Test method for {@link UuidValidator#isValid(String, ConstraintValidatorContext)}.
   */
  @Test
  public void testIsValid() {
    assertFalse(this.validator.isValid(null, this.context));

    assertFalse(this.validator.isValid("", this.context));

    assertFalse(this.validator.isValid("this_is_not_a_UUID", this.context));

    assertFalse(this.validator.isValid("00000000000000000000000000000000", this.context));

    assertTrue(this.validator.isValid("00000000-0000-0000-0000-000000000000", this.context));

    assertTrue(this.validator.isValid("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa", this.context));

    assertTrue(this.validator.isValid("AAAAAAAA-AAAA-AAAA-AAAA-AAAAAAAAAAAA", this.context));

    assertTrue(this.validator.isValid(UUID.randomUUID().toString(), this.context));
  }

}
