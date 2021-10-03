/*
 * Creation by madmath03 the 2018-01-08.
 */

package com.monogramm.starter.persistence;

import static org.junit.Assert.assertEquals;

import com.monogramm.starter.persistence.AbstractToken.AbstractTokenBuilder;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.Date;

import org.junit.Test;

/**
 * {@link AbstractTokenBuilder} Unit Test.
 * 
 * @author madmath03
 */
public abstract class AbstractTokenBuilderTest<
    T extends AbstractTokenBuilder<? extends AbstractToken>>
    extends AbstractGenericEntityBuilderTest<T> {

  /**
   * Test method for {@link AbstractTokenBuilder#code(String)}.
   */
  @Test
  public void testCode() {
    T builder = this.buildTestEntityBuilder();

    assertEquals(builder, builder.code(null));

    final String code = "Test";
    assertEquals(code, builder.code(code).build().getCode());
  }

  /**
   * Test method for {@link AbstractTokenBuilder#expiryDate(java.util.Date)}.
   */
  @Test
  public void testExpiryDate() {
    T builder = this.buildTestEntityBuilder();

    assertEquals(builder, builder.createdAt(null));

    final Date date = new Date();
    assertEquals(date, builder.expiryDate(date).build().getExpiryDate());
  }

  /**
   * Test method for {@link AbstractTokenBuilder#user(User)}.
   */
  @Test
  public void testUser() {
    T builder = this.buildTestEntityBuilder();

    assertEquals(builder, builder.user(null));

    final User user = new User("testUser");
    assertEquals(user, builder.user(user).build().getUser());
  }

}
