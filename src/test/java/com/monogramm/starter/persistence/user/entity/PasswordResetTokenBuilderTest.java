/*
 * Creation by madmath03 the 2018-01-08.
 */

package com.monogramm.starter.persistence.user.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.monogramm.starter.persistence.AbstractTokenBuilderTest;
import com.monogramm.starter.persistence.user.entity.PasswordResetToken.PasswordResetTokenBuilder;

import java.util.Date;

import org.junit.Test;

/**
 * {@link PasswordResetTokenBuilder} Unit Test.
 * 
 * @author madmath03
 */
public class PasswordResetTokenBuilderTest
    extends AbstractTokenBuilderTest<PasswordResetToken.PasswordResetTokenBuilder> {

  protected static final String DUMMY_CODE = "Foo";
  protected static final Date DUMMY_EXPIRY_DATE = new Date();

  @Override
  protected PasswordResetTokenBuilder buildTestEntityBuilder() {
    return PasswordResetToken.builder();
  }

  /**
   * Test method for
   * {@link PasswordResetToken.PasswordResetTokenBuilder#PasswordResetTokenBuilder(String, Date)}.
   */
  @Test
  public void testPasswordResetTokenBuilderStringDate() {
    PasswordResetToken.PasswordResetTokenBuilder builder =
        PasswordResetToken.builder(DUMMY_CODE, DUMMY_EXPIRY_DATE);

    assertNotNull(builder);

    assertEquals(DUMMY_CODE, builder.code(DUMMY_CODE).build().getCode());
    assertEquals(DUMMY_EXPIRY_DATE, builder.expiryDate(DUMMY_EXPIRY_DATE).build().getExpiryDate());
  }

}
