/*
 * Creation by madmath03 the 2017-12-17.
 */

package com.monogramm.starter.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.Date;

import org.junit.Test;

/**
 * {@link AbstractToken} Unit Test.
 * 
 * @author madmath03
 */
public abstract class AbstractTokenTest<T extends AbstractToken>
    extends AbstractGenericEntityTest<T> {

  protected static final String DUMMY_CODE = "Foo";
  protected static final Date DUMMY_EXPIRY_DATE = new Date();

  /**
   * Build an entity to test.
   * 
   * @param token the token.
   * @param expiryDate the token expiry date.
   * 
   * @return an entity for tests.
   */
  abstract protected T buildTestEntity(String token, Date expiryDate);

  /**
   * Build a copy of an entity to test.
   * 
   * @param other the other {@link AbstractToken} to copy.
   * 
   * @return an entity for tests.
   */
  abstract protected T buildTestEntity(T other);

  @Override
  public void testToJson() throws JsonProcessingException {
    super.testToJson();

    this.getEntity().setCode(DUMMY_CODE);
    assertNotNull(this.getEntity().toJson());

    this.getEntity().setExpiryDate(DUMMY_EXPIRY_DATE);
    assertNotNull(this.getEntity().toJson());

    final User testUser = new User(this.getClass().toString());
    this.getEntity().setUser(testUser);
    assertNotNull(this.getEntity().toJson());
  }

  @Override
  public void testToString() {
    super.testToString();

    this.getEntity().setCode(DUMMY_CODE);
    assertNotNull(this.getEntity().toString());

    this.getEntity().setExpiryDate(DUMMY_EXPIRY_DATE);
    assertNotNull(this.getEntity().toString());

    final User testUser = new User(this.getClass().toString());
    this.getEntity().setUser(testUser);
    assertNotNull(this.getEntity().toString());
  }

  @Override
  public void testHashCode() {
    assertEquals(this.getEntity().hashCode(), this.getEntity().hashCode());

    final T copy = this.buildTestEntity(this.getEntity());

    assertEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setCode(DUMMY_CODE);

    assertNotEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setCode(null);

    assertNotEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setExpiryDate(DUMMY_EXPIRY_DATE);

    assertNotEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setExpiryDate(null);

    assertNotEquals(copy.hashCode(), this.getEntity().hashCode());
  }

  @Override
  public void testEqualsObject() {
    assertEquals(this.getEntity(), this.getEntity());
    assertNotEquals(this.getEntity(), null);
    assertNotEquals(this.getEntity(), new Object());


    final T anotherEntity = this.buildTestEntity();

    assertNotEquals(this.getEntity(), anotherEntity);
    assertNotEquals(anotherEntity, this.getEntity());


    final T copy = this.buildTestEntity(this.getEntity());

    assertEquals(copy, this.getEntity());

    copy.setCode(DUMMY_CODE);

    assertNotEquals(copy, this.getEntity());

    copy.setCode(null);

    assertNotEquals(copy, this.getEntity());

    copy.setExpiryDate(DUMMY_EXPIRY_DATE);

    assertNotEquals(copy, this.getEntity());

    copy.setExpiryDate(null);

    assertNotEquals(copy, this.getEntity());
  }

  /**
   * Test method for {@link AbstractToken#calculateExpiryDate()}.
   */
  @Test
  public void testCalculateExpiryDate() {
    final Date nowBefore = new Date();
    final Date date = AbstractToken.calculateExpiryDate();
    final Date nowAfter = new Date();

    assertNotNull(date);
    assertTrue(date.getTime() > nowBefore.getTime());
    assertTrue(date.getTime() - nowAfter.getTime() <= AbstractToken.EXPIRATION * 60_000L);
  }


  /**
   * Test method for {@link AbstractToken#calculateExpiryDate(int)}.
   */
  @Test
  public void testCreateRandomReadableToken() {
    final int length = 6;
    final String token = AbstractToken.createRandomToken(length);

    assertNotNull(token);
    assertEquals(length, token.length());
  }

  /**
   * Test method for {@link AbstractToken#calculateExpiryDate(int)}.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCreateRandomReadableTokenIllegalArgumentException() {
    AbstractToken.createRandomToken(-1);
  }

  /**
   * Test method for {@link AbstractToken#calculateExpiryDate(int)}.
   */
  @Test
  public void testCalculateExpiryDateInt() {
    final int duration = -42;
    final Date date = AbstractToken.calculateExpiryDate(duration);

    assertNotNull(date);

    final Date now = new Date();
    assertTrue(date.getTime() < now.getTime());
  }

  /**
   * Test method for {@link AbstractToken#AbstractToken()}.
   */
  @Test
  public void testAbstractToken() {
    final AbstractToken token = this.buildTestEntity();

    assertNotNull(token.getCode());
    assertNotNull(token.getExpiryDate());
    assertNull(token.getUser());
  }

  /**
   * Test method for {@link AbstractToken#AbstractToken(java.lang.String, java.util.Date)}.
   */
  @Test
  public void testAbstractTokenStringDate() {
    final AbstractToken token = this.buildTestEntity(DUMMY_CODE, DUMMY_EXPIRY_DATE);

    assertEquals(DUMMY_CODE, token.getCode());
    assertEquals(DUMMY_EXPIRY_DATE, token.getExpiryDate());
    assertNull(token.getUser());
  }

  /**
   * Test method for {@link AbstractToken#AbstractToken(java.lang.String, java.util.Date)}.
   */
  @Test
  public void testAbstractTokenNullNull() {
    final AbstractToken token = this.buildTestEntity(null, null);

    assertNull(token.getCode());
    assertNull(token.getExpiryDate());
    assertNull(token.getUser());
  }

  /**
   * Test method for {@link AbstractToken#AbstractToken(AbstractToken)}.
   */
  @Test
  public void testAbstractTokenAbstractToken() {
    final AbstractToken test = this.buildTestEntity(this.getEntity());

    assertNotNull(test);
    assertEquals(test, this.getEntity());
  }

  /**
   * Test method for {@link AbstractToken#update(AbstractToken)}.
   */
  @Test
  public void testUpdateAbstractToken() {
    final AbstractToken anotherEntity = this.buildTestEntity();

    this.getEntity().update(anotherEntity);

    assertNotSame(this.getEntity(), anotherEntity);

    assertNotNull(this.getEntity().getCode());
    assertEquals(this.getEntity().getCode(), anotherEntity.getCode());
    assertNotNull(this.getEntity().getExpiryDate());
    assertEquals(this.getEntity().getExpiryDate(), anotherEntity.getExpiryDate());
    assertNull(this.getEntity().getUser());
    assertEquals(this.getEntity().getUser(), anotherEntity.getUser());
  }

  /**
   * Test method for {@link AbstractToken#getCode()}.
   */
  @Test
  public void testGetCode() {
    assertNotNull(this.getEntity().getCode());
  }

  /**
   * Test method for {@link AbstractToken#setCode(java.lang.String)}.
   */
  @Test
  public void testSetCode() {
    final String test = "TEST";
    this.getEntity().setCode(test);
    assertEquals(test, this.getEntity().getCode());
  }

  /**
   * Test method for {@link AbstractToken#getExpiryDate()}.
   */
  @Test
  public void testGetExpiryDate() {
    assertNotNull(this.getEntity().getExpiryDate());
  }

  /**
   * Test method for {@link AbstractToken#setExpiryDate(java.util.Date)}.
   */
  @Test
  public void testSetExpiryDate() {
    final Date date = new Date();
    this.getEntity().setExpiryDate(date);
    assertEquals(date, this.getEntity().getExpiryDate());

    final Date newDate = new Date();
    this.getEntity().setExpiryDate(newDate);
    assertEquals(newDate, this.getEntity().getExpiryDate());
  }

  /**
   * Test method for {@link AbstractToken#getUser()}.
   */
  @Test
  public void testGetUser() {
    assertNull(this.getEntity().getUser());
  }

  /**
   * Test method for {@link AbstractToken#setUser(User)}.
   */
  @Test
  public void testSetUser() {
    final User testUser = new User(this.getClass().toString());
    this.getEntity().setUser(testUser);
    assertEquals(testUser, this.getEntity().getUser());
  }

}
