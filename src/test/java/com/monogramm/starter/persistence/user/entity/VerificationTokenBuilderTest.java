/*
 * Creation by madmath03 the 2018-01-08.
 */

package com.monogramm.starter.persistence.user.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.monogramm.starter.persistence.AbstractTokenBuilderTest;
import com.monogramm.starter.persistence.user.entity.VerificationToken.VerificationTokenBuilder;

import java.util.Date;

import org.junit.Test;

/**
 * {@link VerificationTokenBuilder} Unit Test.
 * 
 * @author madmath03
 */
public class VerificationTokenBuilderTest
    extends AbstractTokenBuilderTest<VerificationToken.VerificationTokenBuilder> {

  protected static final String DUMMY_CODE = "Foo";
  protected static final Date DUMMY_EXPIRY_DATE = new Date();

  @Override
  protected VerificationTokenBuilder buildTestEntityBuilder() {
    return VerificationToken.builder();
  }

  /**
   * Test method for
   * {@link VerificationToken.VerificationTokenBuilder#VerificationTokenBuilder(String, Date)}.
   */
  @Test
  public void testVerificationTokenBuilderStringDate() {
    VerificationToken.VerificationTokenBuilder builder =
        VerificationToken.builder(DUMMY_CODE, DUMMY_EXPIRY_DATE);

    assertNotNull(builder);

    assertEquals(DUMMY_CODE, builder.code(DUMMY_CODE).build().getCode());
    assertEquals(DUMMY_EXPIRY_DATE, builder.expiryDate(DUMMY_EXPIRY_DATE).build().getExpiryDate());
  }

  /**
   * Test method for {@link VerificationToken.VerificationTokenBuilder#field(java.lang.String)}.
   */
  @Test
  public void testField() {
    VerificationToken.VerificationTokenBuilder builder = this.buildTestEntityBuilder();

    assertEquals(builder, builder.field(null));

    final String field = "Test";
    assertEquals(field, builder.field(field).build().getField());
  }

  /**
   * Test method for {@link VerificationToken.VerificationTokenBuilder#value(java.lang.String)}.
   */
  @Test
  public void testValue() {
    VerificationToken.VerificationTokenBuilder builder = this.buildTestEntityBuilder();

    assertEquals(builder, builder.value(null));

    final String value = "Test";
    assertEquals(value, builder.value(value).build().getValue());
  }

}
