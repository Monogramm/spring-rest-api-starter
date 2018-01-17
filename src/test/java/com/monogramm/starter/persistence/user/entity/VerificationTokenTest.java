/*
 * Creation by madmath03 the 2017-12-17.
 */

package com.monogramm.starter.persistence.user.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import com.monogramm.starter.persistence.AbstractTokenTest;

import java.util.Date;

import org.junit.Test;

/**
 * {@link VerificationToken} Unit Test.
 * 
 * @author madmath03
 */
public class VerificationTokenTest extends AbstractTokenTest<VerificationToken> {

  private static final String DUMMY_FIELD = "email";
  private static final String DUMMY_VALUE = "my_email@address.com";

  @Override
  protected VerificationToken buildTestEntity(String token, Date expiryDate) {
    return new VerificationToken(token, expiryDate);
  }

  @Override
  protected VerificationToken buildTestEntity(VerificationToken other) {
    return new VerificationToken(other);
  }

  @Override
  protected VerificationToken buildTestEntity() {
    return new VerificationToken();
  }

  /**
   * Test method for
   * {@link VerificationToken#VerificationToken(String, java.util.Date, String, String)}.
   */
  @Test
  public void testVerificationTokenStringDateStringString() {
    final VerificationToken token =
        new VerificationToken(DUMMY_CODE, DUMMY_EXPIRY_DATE, DUMMY_FIELD, DUMMY_VALUE);

    assertEquals(DUMMY_CODE, token.getCode());
    assertEquals(DUMMY_EXPIRY_DATE, token.getExpiryDate());
    assertNull(token.getUser());

    assertEquals(DUMMY_FIELD, token.getField());
    assertEquals(DUMMY_VALUE, token.getValue());
  }

  /**
   * Test method for {@link VerificationToken#getField()}.
   */
  @Test
  public void testGetField() {
    assertNull(this.getEntity().getField());
  }

  /**
   * Test method for {@link VerificationToken#setField(java.lang.String)}.
   */
  @Test
  public void testSetField() {
    final String test = "TEST";
    this.getEntity().setField(test);
    assertEquals(test, this.getEntity().getField());
  }

  /**
   * Test method for {@link VerificationToken#getValue()}.
   */
  @Test
  public void testGetValue() {
    assertNull(this.getEntity().getValue());
  }

  /**
   * Test method for {@link VerificationToken#setValue(java.lang.String)}.
   */
  @Test
  public void testSetValue() {
    final String test = "TEST";
    this.getEntity().setValue(test);
    assertEquals(test, this.getEntity().getValue());
  }

  /**
   * Test method for {@link VerificationToken#hashCode()}.
   */
  @Test
  public void testHashCode() {
    super.testHashCode();

    final VerificationToken copy = this.buildTestEntity(this.getEntity());

    copy.setField(DUMMY_FIELD);

    assertNotEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setField(null);

    assertEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setValue(DUMMY_VALUE);

    assertNotEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setValue(null);

    assertEquals(copy.hashCode(), this.getEntity().hashCode());
  }

  /**
   * Test method for {@link VerificationToken#equals(java.lang.Object)}.
   */
  @Test
  public void testEqualsObject() {
    super.testEqualsObject();

    final VerificationToken copy = this.buildTestEntity(this.getEntity());

    copy.setField(DUMMY_FIELD);

    assertNotEquals(copy, this.getEntity());

    copy.setField(null);

    assertEquals(copy, this.getEntity());

    copy.setValue(DUMMY_VALUE);

    assertNotEquals(copy, this.getEntity());

    copy.setValue(null);

    assertEquals(copy, this.getEntity());
  }

}
