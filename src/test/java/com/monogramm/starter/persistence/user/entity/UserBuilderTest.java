/*
 * Creation by madmath03 the 2017-08-27.
 */

package com.monogramm.starter.persistence.user.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.github.madmath03.password.Passwords;
import com.monogramm.starter.persistence.AbstractGenericEntityBuilderTest;
import com.monogramm.starter.persistence.role.entity.Role;
import com.monogramm.starter.persistence.user.entity.User.UserBuilder;

import org.junit.Test;

/**
 * {@link UserBuilder} Unit Test.
 * 
 * @author madmath03
 */
public class UserBuilderTest extends AbstractGenericEntityBuilderTest<User.UserBuilder> {

  private static final String USERNAME = "TEST";

  private static final String EMAIL = "TEST@TEST.COM";

  @Override
  protected UserBuilder buildTestEntityBuilder() {
    return User.builder();
  }

  /**
   * Test method for {@link User#builder()}.
   */
  @Test
  public void testGetBuilder() {
    User.UserBuilder builder = User.builder();

    assertNotNull(builder);

    final User userBuilt = builder.build();
    assertNotNull(userBuilt);
    assertNull(userBuilt.getUsername());
    assertNull(userBuilt.getEmail());
    assertNull(userBuilt.getPassword());
    assertTrue(userBuilt.isEnabled());
    assertFalse(userBuilt.isVerified());
    assertNull(userBuilt.getRole());
  }

  /**
   * Test method for
   * {@link User#builder(java.lang.String)}.
   */
  @Test
  public void testGetBuilderString() {
    User.UserBuilder builder = User.builder(USERNAME);

    assertNotNull(builder);

    final User userBuilt = builder.build();
    assertNotNull(userBuilt);
    assertEquals(USERNAME, userBuilt.getUsername());
    assertNull(userBuilt.getEmail());
    assertNull(userBuilt.getPassword());
    assertTrue(userBuilt.isEnabled());
    assertFalse(userBuilt.isVerified());
    assertNull(userBuilt.getRole());
  }

  /**
   * Test method for
   * {@link User#builder(java.lang.String, java.lang.String)}.
   */
  @Test
  public void testGetBuilderStringString() {
    User.UserBuilder builder = User.builder(USERNAME, EMAIL);

    assertNotNull(builder);

    final User userBuilt = builder.build();
    assertNotNull(userBuilt);
    assertEquals(USERNAME, userBuilt.getUsername());
    assertEquals(EMAIL, userBuilt.getEmail());
    assertNull(userBuilt.getPassword());
    assertTrue(userBuilt.isEnabled());
    assertFalse(userBuilt.isVerified());
    assertNull(userBuilt.getRole());
  }

  /**
   * Test method for
   * {@link User.UserBuilder#username(java.lang.String)}.
   */
  @Test
  public void testUsername() {
    assertEquals(this.getEntityBuilder(), this.getEntityBuilder().username(null));

    final String username = "TEST";
    assertEquals(username, this.getEntityBuilder().username(username).build().getUsername());
  }

  /**
   * Test method for
   * {@link User.UserBuilder#email(java.lang.String)}.
   */
  @Test
  public void testEmail() {
    assertEquals(this.getEntityBuilder(), this.getEntityBuilder().email(null));

    final String email = "TEST@EMAIL.COM";
    assertEquals(email, this.getEntityBuilder().email(email).build().getEmail());
  }

  /**
   * Test method for
   * {@link User.UserBuilder#password(char[])}.
   */
  @Test
  public void testPasswordCharArray() {
    assertNull(this.getEntityBuilder().password((char[]) null).build().getPassword());

    final char[] password = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
    assertTrue(Passwords.isExpectedPassword(password,
        this.getEntityBuilder().password(password.clone()).build().getPassword()));
  }

  /**
   * Test method for
   * {@link User.UserBuilder#password(java.lang.String)}.
   */
  @Test
  public void testPasswordString() {
    assertNull(this.getEntityBuilder().password((String) null).build().getPassword());

    final String password = "PASSWORD";
    assertEquals(password, this.getEntityBuilder().password(password).build().getPassword());
  }

  /**
   * Test method for
   * {@link User.UserBuilder#enabled(boolean)}.
   */
  @Test
  public void testEnabled() {
    assertEquals(this.getEntityBuilder(), this.getEntityBuilder().enabled(false));

    final boolean enabled = false;
    assertEquals(enabled, this.getEntityBuilder().enabled(enabled).build().isEnabled());
  }

  /**
   * Test method for
   * {@link User.UserBuilder#verified(boolean)}.
   */
  @Test
  public void testVerified() {
    assertEquals(this.getEntityBuilder(), this.getEntityBuilder().verified(false));

    final boolean verified = true;
    assertEquals(verified, this.getEntityBuilder().verified(verified).build().isVerified());
  }

  /**
   * Test method for
   * {@link User.UserBuilder#role(Role)}.
   */
  @Test
  public void testRole() {
    assertEquals(this.getEntityBuilder(), this.getEntityBuilder().role((Role) null));

    final Role role = new Role("testRole");
    assertEquals(role, this.getEntityBuilder().role(role).build().getRole());
  }

}
