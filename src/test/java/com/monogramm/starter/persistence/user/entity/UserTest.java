/*
 * Creation by madmath03 the 2017-08-27.
 */

package com.monogramm.starter.persistence.user.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.madmath03.password.Passwords;
import com.monogramm.starter.persistence.AbstractGenericEntityTest;
import com.monogramm.starter.persistence.role.entity.Role;

import java.util.UUID;

import org.junit.Test;

/**
 * {@link User} Unit Test.
 * 
 * @author madmath03
 */
public class UserTest extends AbstractGenericEntityTest<User> {

  private static final String USERNAME = "Foo";
  private static final String EMAIL = "foo@email.com";

  @Override
  protected User buildTestEntity() {
    return User.builder().build();
  }

  protected User buildTestEntity(final String username) {
    return User.builder(username).build();
  }

  protected User buildTestEntity(final String username, final String email) {
    return User.builder(username, email).build();
  }

  @Override
  public void testToJSON() throws JsonProcessingException {
    super.testToJSON();

    final Role testRole = Role.builder(this.getClass().toString()).id(UUID.randomUUID()).build();
    this.getEntity().setRole(testRole);
    assertNotNull(this.getEntity().toJson());

    testRole.addUser(this.getEntity());
    assertNotNull(this.getEntity().toJson());
  }

  @Override
  public void testToString() {
    super.testToString();

    final Role testRole = new Role(this.getClass().toString());
    this.getEntity().setRole(testRole);
    assertNotNull(this.getEntity().toString());

    testRole.addUser(this.getEntity());
    assertNotNull(this.getEntity().toString());
  }

  @Override
  public void testHashCode() {
    super.testHashCode();

    final User copy = new User(this.getEntity());

    assertEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setUsername(USERNAME);

    assertNotEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setUsername(null);

    assertEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setEmail(EMAIL);

    assertNotEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setEmail(null);

    assertEquals(copy.hashCode(), this.getEntity().hashCode());
  }

  @Override
  public void testEqualsObject() {
    super.testEqualsObject();

    final User copy = new User(this.getEntity());

    assertEquals(copy, this.getEntity());

    copy.setUsername(USERNAME);

    assertNotEquals(copy, this.getEntity());

    copy.setUsername(null);

    assertEquals(copy, this.getEntity());

    copy.setEmail(EMAIL);

    assertNotEquals(copy.hashCode(), this.getEntity().hashCode());

    copy.setEmail(null);

    assertEquals(copy.hashCode(), this.getEntity().hashCode());
  }

  /**
   * Test method for {@link User#User()}.
   */
  @Test
  public void testUser() {
    final User user = new User();

    assertNull(user.getUsername());
    assertNull(user.getEmail());
  }

  /**
   * Test method for {@link User#User(String)}.
   */
  @Test
  public void testUserString() {
    final String username = "TEST";
    final User user = new User(username);

    assertEquals(username, user.getUsername());
    assertNull(user.getEmail());
  }

  /**
   * Test method for
   * {@link User#User(String, String)}.
   */
  @Test
  public void testUserStringString() {
    final String username = "TEST";
    final String email = "TEST@EMAIL.COM";
    final User user = new User(username, email);

    assertEquals(username, user.getUsername());
    assertEquals(email, user.getEmail());
  }

  /**
   * Test method for {@link User#User()}.
   */
  @Test
  public void testUserUser() {
    final User test = new User(this.getEntity());

    assertNotNull(test);
    assertEquals(test, this.getEntity());
  }

  /**
   * Test method for {@link User#getUsername()}.
   */
  @Test
  public void testGetUsername() {
    assertNull(this.getEntity().getUsername());
  }

  /**
   * Test method for
   * {@link User#setUsername(java.lang.String)}.
   */
  @Test
  public void testSetUsername() {
    final String username = "TEST";
    this.getEntity().setUsername(username);
    assertEquals(username, this.getEntity().getUsername());
  }

  /**
   * Test method for {@link User#getEmail()}.
   */
  @Test
  public void testGetEmail() {
    assertNull(this.getEntity().getEmail());
  }

  /**
   * Test method for
   * {@link User#setEmail(java.lang.String)}.
   */
  @Test
  public void testSetEmail() {
    final String email = "TEST@EMAIL.COM";
    this.getEntity().setEmail(email);
    assertEquals(email, this.getEntity().getEmail());
    assertFalse(this.getEntity().isVerified());

    final String newEmail = "NEWTEST@EMAIL.COM";
    this.getEntity().setVerified(true);
    this.getEntity().setEmail(newEmail);
    assertEquals(newEmail, this.getEntity().getEmail());
    assertFalse(this.getEntity().isVerified());
  }

  /**
   * Test method for {@link User#getPassword()}.
   */
  @Test
  public void testGetPassword() {
    assertNull(this.getEntity().getPassword());
  }

  /**
   * Test method for
   * {@link User#setPassword(char...)}.
   */
  @Test
  public void testSetPasswordCharArray() {
    final char[] password = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
    this.getEntity().setPassword(password.clone());
    assertNotNull(this.getEntity().getPassword());
    assertTrue(Passwords.isExpectedPassword(password, this.getEntity().getPassword()));
  }

  /**
   * Test method for
   * {@link User#setPassword(java.lang.String)}.
   */
  @Test
  public void testSetPasswordString() {
    final String password = "PASSWORD";
    this.getEntity().setPassword(password);
    assertEquals(password, this.getEntity().getPassword());
  }

  /**
   * Test method for {@link User#isEnabled()}.
   */
  @Test
  public void testIsEnabled() {
    assertTrue(this.getEntity().isEnabled());
  }

  /**
   * Test method for
   * {@link User#setEnabled(java.lang.Boolean)}.
   */
  @Test
  public void testSetEnabled() {
    final boolean enabled = false;
    this.getEntity().setEnabled(enabled);
    assertEquals(enabled, this.getEntity().isEnabled());
  }

  /**
   * Test method for {@link User#isVerified()}.
   */
  @Test
  public void testIsVerified() {
    assertFalse(this.getEntity().isVerified());
  }

  /**
   * Test method for
   * {@link User#setVerified(java.lang.Boolean)}.
   */
  @Test
  public void testSetVerified() {
    final boolean verified = true;
    this.getEntity().setVerified(verified);
    assertEquals(verified, this.getEntity().isVerified());
  }

  /**
   * Test method for {@link User#getRole()}.
   */
  @Test
  public void testGetRole() {
    assertNull(this.getEntity().getRole());
  }

  /**
   * Test method for
   * {@link User#setRole(java.util.UUID)}.
   */
  @Test
  public void testSetRole() {
    final Role role = Role.builder("testRole").build();
    this.getEntity().setRole(role);
    assertEquals(role, this.getEntity().getRole());
  }

  /**
   * Test method for {@link User#update(User)}.
   */
  @Test
  public void testUpdate() {
    super.testUpdate();

    final User anotherEntity = this.buildTestEntity();

    this.getEntity().update(anotherEntity);

    assertNotSame(this.getEntity(), anotherEntity);

    assertNull(this.getEntity().getUsername());
    assertEquals(this.getEntity().getUsername(), anotherEntity.getUsername());
    assertNull(this.getEntity().getEmail());
    assertEquals(this.getEntity().getEmail(), anotherEntity.getEmail());
    assertEquals(this.getEntity().getPassword(), anotherEntity.getPassword());
    assertEquals(this.getEntity().isEnabled(), anotherEntity.isEnabled());
    assertEquals(this.getEntity().isVerified(), anotherEntity.isVerified());
    assertEquals(this.getEntity().getRole(), anotherEntity.getRole());
  }

  /**
   * Test method for {@link User#update(User)}.
   */
  @Test
  public void testUpdateUsername() {
    final User anotherEntity = this.buildTestEntity(USERNAME);

    this.getEntity().update(anotherEntity);

    assertNotSame(this.getEntity(), anotherEntity);

    assertNotNull(this.getEntity().getUsername());
    assertEquals(this.getEntity().getUsername(), anotherEntity.getUsername());
    assertNull(this.getEntity().getEmail());
    assertEquals(this.getEntity().getEmail(), anotherEntity.getEmail());
    assertEquals(this.getEntity().getPassword(), anotherEntity.getPassword());
    assertEquals(this.getEntity().isEnabled(), anotherEntity.isEnabled());
    assertEquals(this.getEntity().isVerified(), anotherEntity.isVerified());
    assertEquals(this.getEntity().getRole(), anotherEntity.getRole());
  }

  /**
   * Test method for {@link User#update(User)}.
   */
  @Test
  public void testUpdateUsernameEmail() {
    final User anotherEntity = this.buildTestEntity(USERNAME, EMAIL);

    this.getEntity().update(anotherEntity);

    assertNotSame(this.getEntity(), anotherEntity);

    assertNotNull(this.getEntity().getUsername());
    assertEquals(this.getEntity().getUsername(), anotherEntity.getUsername());
    assertNotNull(this.getEntity().getEmail());
    assertEquals(this.getEntity().getEmail(), anotherEntity.getEmail());
    assertEquals(this.getEntity().getPassword(), anotherEntity.getPassword());
    assertEquals(this.getEntity().isEnabled(), anotherEntity.isEnabled());
    assertEquals(this.getEntity().isVerified(), anotherEntity.isVerified());
    assertEquals(this.getEntity().getRole(), anotherEntity.getRole());
  }

  /**
   * Test method for {@link User#update(User)}.
   */
  @Test
  public void testUpdateFull() {
    final User anotherEntity = this.buildTestEntity(USERNAME, EMAIL);
    anotherEntity.setPassword("testPassword");
    anotherEntity.setEnabled(!anotherEntity.isEnabled());
    anotherEntity.setVerified(!anotherEntity.isVerified());
    anotherEntity.setRole(new Role());

    this.getEntity().update(anotherEntity);

    assertNotSame(this.getEntity(), anotherEntity);

    assertNotNull(this.getEntity().getUsername());
    assertEquals(this.getEntity().getUsername(), anotherEntity.getUsername());
    assertNotNull(this.getEntity().getEmail());
    assertEquals(this.getEntity().getEmail(), anotherEntity.getEmail());
    assertEquals(this.getEntity().getPassword(), anotherEntity.getPassword());
    assertNotEquals(this.getEntity().isEnabled(), anotherEntity.isEnabled());
    assertNotEquals(this.getEntity().isVerified(), anotherEntity.isVerified());
    assertEquals(this.getEntity().getRole(), anotherEntity.getRole());


    anotherEntity.setPassword("testPassword");
    anotherEntity.setEnabled(!anotherEntity.isEnabled());
    anotherEntity.setVerified(!anotherEntity.isVerified());
    anotherEntity.setRole(new Role());

    this.getEntity().update(anotherEntity);

    assertNotSame(this.getEntity(), anotherEntity);

    assertNotNull(this.getEntity().getUsername());
    assertEquals(this.getEntity().getUsername(), anotherEntity.getUsername());
    assertNotNull(this.getEntity().getEmail());
    assertEquals(this.getEntity().getEmail(), anotherEntity.getEmail());
    assertEquals(this.getEntity().getPassword(), anotherEntity.getPassword());
    assertEquals(this.getEntity().isEnabled(), anotherEntity.isEnabled());
    assertEquals(this.getEntity().isVerified(), anotherEntity.isVerified());
    assertEquals(this.getEntity().getRole(), anotherEntity.getRole());
  }

}
