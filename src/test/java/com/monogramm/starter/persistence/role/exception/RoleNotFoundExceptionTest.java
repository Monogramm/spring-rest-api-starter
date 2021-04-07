/*
 * Creation by madmath03 the 2017-09-04.
 */

package com.monogramm.starter.persistence.role.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.monogramm.starter.persistence.role.exception.RoleNotFoundException;

import org.junit.Test;

/**
 * {@link RoleNotFoundException} Unit Test.
 * 
 * @author madmath03
 */
public class RoleNotFoundExceptionTest {

  private static final String MESSAGE = "Foo";
  private static final Throwable CAUSE = new Exception();

  /**
   * Test method for {@link RoleNotFoundException#RoleNotFoundException()}.
   */
  @Test
  public void testRoleNotFoundException() {
    final RoleNotFoundException exception = new RoleNotFoundException();

    assertNotNull(exception);
  }

  /**
   * Test method for {@link RoleNotFoundException#RoleNotFoundException(java.lang.String)}.
   */
  @Test
  public void testRoleNotFoundExceptionString() {
    final RoleNotFoundException exception = new RoleNotFoundException(MESSAGE);

    assertEquals(MESSAGE, exception.getMessage());
  }

  /**
   * Test method for {@link RoleNotFoundException#RoleNotFoundException(java.lang.Throwable)}.
   */
  @Test
  public void testRoleNotFoundExceptionThrowable() {
    final RoleNotFoundException exception = new RoleNotFoundException(CAUSE);

    assertEquals(CAUSE, exception.getCause());
  }

  /**
   * Test method for
   * {@link RoleNotFoundException#RoleNotFoundException(java.lang.String, java.lang.Throwable)}.
   */
  @Test
  public void testRoleNotFoundExceptionStringThrowable() {
    final RoleNotFoundException exception = new RoleNotFoundException(MESSAGE, CAUSE);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());
  }

  /**
   * Test method for
   * {@link RoleNotFoundException#RoleNotFoundException(java.lang.String, java.lang.Throwable, boolean, boolean)}.
   */
  @Test
  public void testRoleNotFoundExceptionStringThrowableBooleanBoolean() {
    RoleNotFoundException exception = new RoleNotFoundException(MESSAGE, CAUSE, true, true);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new RoleNotFoundException(MESSAGE, CAUSE, true, false);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new RoleNotFoundException(MESSAGE, CAUSE, false, true);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());

    exception = new RoleNotFoundException(MESSAGE, CAUSE, false, false);

    assertEquals(MESSAGE, exception.getMessage());
    assertEquals(CAUSE, exception.getCause());
  }

}
