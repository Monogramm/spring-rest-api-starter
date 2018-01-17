/*
 * Creation by madmath03 the 2017-09-12.
 */

package com.monogramm.starter.persistence.user.dao;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import com.monogramm.starter.config.data.InitialDataLoader;
import com.monogramm.starter.persistence.AbstractGenericRepositoryIT;
import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.persistence.user.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * {@link IUserRepository} Integration Test.
 * 
 * @author madmath03
 */
public class IUserRepositoryIT extends AbstractGenericRepositoryIT<User, IUserRepository> {

  private static final String USERNAME = "Foo";
  private static final String EMAIL = "foo@email.com";
  private static final char[] PASSWORD = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};

  @Autowired
  private InitialDataLoader initialDataLoader;

  private char[] testPassword;

  @Before
  public void setUp() {
    testPassword = PASSWORD.clone();
  }

  @Override
  protected User buildTestEntity() {
    return User.builder(USERNAME, EMAIL).build();
  }

  /**
   * Test method for {@link IUserRepository#findAll()}.
   */
  @Override
  @Test
  public void testFindAll() {
    int expectedSize = 0;
    // ...plus the permissions created at application initialization
    if (initialDataLoader.getUsers() != null) {
      expectedSize += initialDataLoader.getUsers().size();
    }

    final List<User> actual = getRepository().findAll();

    assertNotNull(actual);
    assertEquals(expectedSize, actual.size());
  }

  /**
   * Test method for {@link IUserRepository#findAllContainingUsernameIgnoreCase(java.lang.String)}.
   */
  @Test
  public void testFindAllContainingUsernameIgnoreCase() {
    final List<User> models = new ArrayList<>();

    final List<User> actual = getRepository().findAllContainingUsernameIgnoreCase(USERNAME);

    assertThat(actual, is(models));
  }

  /**
   * Test method for {@link IUserRepository#findAllContainingEmailIgnoreCase(java.lang.String)}.
   */
  @Test
  public void testFindAllContainingEmailIgnoreCase() {
    final List<User> models = new ArrayList<>();

    final List<User> actual = getRepository().findAllContainingEmailIgnoreCase(EMAIL);

    assertThat(actual, is(models));
  }

  /**
   * Test method for
   * {@link IUserRepository#findAllContainingUsernameOrEmailIgnoreCase(java.lang.String, java.lang.String)}.
   */
  @Test
  public void testFindAllContainingUsernameOrEmailIgnoreCase() {
    final List<User> models = new ArrayList<>();

    final List<User> actual =
        getRepository().findAllContainingUsernameOrEmailIgnoreCase(USERNAME, EMAIL);

    assertThat(actual, is(models));
  }

  /**
   * Test method for {@link IUserRepository#findByUsernameIgnoreCase(java.lang.String)}.
   * 
   * @throws UserNotFoundException if the user is not found.
   */
  @Test
  public void testFindByUsernameIgnoreCase() {
    final User model = User.builder(USERNAME.toUpperCase(), EMAIL.toUpperCase()).build();
    getRepository().add(model);

    final User actual = getRepository().findByUsernameIgnoreCase(USERNAME);

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link IUserRepository#findByUsernameIgnoreCase(java.lang.String)}.
   * 
   * @throws UserNotFoundException if the user is not found.
   */
  @Test
  public void testFindByUsernameIgnoreCaseNoResult() {
    assertNull(getRepository().findByUsernameIgnoreCase(USERNAME));
  }

  /**
   * Test method for {@link IUserRepository#findByUsernameIgnoreCase(java.lang.String)}.
   * 
   * @throws UserNotFoundException if the user is not found.
   */
  @Test
  public void testFindByUsernameIgnoreCaseNonUnique() {
    getRepository().add(User.builder(USERNAME + "1", EMAIL + "1").build());
    getRepository().add(User.builder(USERNAME + "2", EMAIL + "2").build());

    assertNull(getRepository().findByUsernameIgnoreCase(USERNAME));
  }

  /**
   * Test method for {@link IUserRepository#findByUsernameIgnoreCase(java.lang.String)}.
   * 
   * @throws UserNotFoundException if the user is not found.
   */
  @Test
  public void testFindByUsernameIgnoreCaseNotFound() {
    assertNull(getRepository().findByUsernameIgnoreCase(null));
  }

  /**
   * Test method for {@link IUserRepository#findByEmailIgnoreCase(java.lang.String)}.
   * 
   * @throws UserNotFoundException if the user is not found.
   */
  @Test
  public void testFindByEmailIgnoreCase() {
    final User model = User.builder(USERNAME.toUpperCase(), EMAIL.toUpperCase()).build();
    getRepository().add(model);

    final User actual = getRepository().findByEmailIgnoreCase(EMAIL);

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link IUserRepository#findByEmailIgnoreCase(java.lang.String)}.
   * 
   * @throws UserNotFoundException if the user is not found.
   */
  @Test
  public void testFindByEmailIgnoreCaseNoResult() {
    assertNull(getRepository().findByEmailIgnoreCase(EMAIL));
  }

  /**
   * Test method for {@link IUserRepository#findByEmailIgnoreCase(java.lang.String)}.
   * 
   * @throws UserNotFoundException if the user is not found.
   */
  @Test
  public void testFindByEmailIgnoreCaseNonUnique() {
    getRepository().add(User.builder(USERNAME + "1", EMAIL + "1").build());
    getRepository().add(User.builder(USERNAME + "2", EMAIL + "2").build());

    assertNull(getRepository().findByEmailIgnoreCase(EMAIL));
  }

  /**
   * Test method for {@link IUserRepository#findByEmailIgnoreCase(java.lang.String)}.
   * 
   * @throws UserNotFoundException if the user is not found.
   */
  @Test
  public void testFindByEmailIgnoreCaseNotFound() {
    assertNull(getRepository().findByEmailIgnoreCase(null));
  }

  /**
   * Test method for
   * {@link IUserRepository#findByUsernameOrEmailIgnoreCase(java.lang.String, java.lang.String)}.
   * 
   * @throws UserNotFoundException if the user is not found.
   */
  @Test
  public void testFindByUsernameOrEmailIgnoreCase() {
    final User model = User.builder(USERNAME.toUpperCase(), EMAIL.toUpperCase()).build();
    getRepository().add(model);

    final User actual = getRepository().findByUsernameOrEmailIgnoreCase(USERNAME, EMAIL);

    assertThat(actual, is(model));
  }

  /**
   * Test method for
   * {@link IUserRepository#findByUsernameOrEmailIgnoreCase(java.lang.String, java.lang.String)}.
   * 
   * @throws UserNotFoundException if the user is not found.
   */
  @Test
  public void testFindByUsernameOrEmailIgnoreCaseNoResult() {
    assertNull(getRepository().findByUsernameOrEmailIgnoreCase(USERNAME, EMAIL));
  }

  /**
   * Test method for
   * {@link IUserRepository#findByUsernameOrEmailIgnoreCase(java.lang.String, java.lang.String)}.
   * 
   * @throws UserNotFoundException if the user is not found.
   */
  @Test
  public void testFindByUsernameOrEmailIgnoreCaseNonUnique() {
    getRepository().add(User.builder(USERNAME + "1", EMAIL + "1").build());
    getRepository().add(User.builder(USERNAME + "2", EMAIL + "2").build());


    getRepository().findByUsernameOrEmailIgnoreCase(USERNAME, EMAIL);
  }

  /**
   * Test method for
   * {@link IUserRepository#findByUsernameOrEmailIgnoreCase(java.lang.String, java.lang.String)}.
   * 
   * @throws UserNotFoundException if the user is not found.
   */
  @Test
  public void testFindByUsernameOrEmailIgnoreCaseNotFound() {
    final User model = null;

    final User actual = getRepository().findByUsernameOrEmailIgnoreCase(USERNAME, EMAIL);

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link IUserRepository#setPassword(java.util.UUID, char[])}.
   */
  @Test
  public void testSetPassword() {
    final User model = User.builder(USERNAME, EMAIL).build();
    getRepository().add(model);

    final User actual = getRepository().setPassword(model.getId(), testPassword);

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link IUserRepository#setPassword(java.util.UUID, char[])}.
   */
  @Test
  public void testSetPasswordNotFound() {
    assertNull(getRepository().setPassword(RANDOM_ID, testPassword));
  }

  /**
   * Test method for {@link IUserRepository#setEnabled(java.util.UUID, boolean)}.
   */
  @Test
  public void testSetEnabled() {
    final User model = User.builder(USERNAME, EMAIL).build();
    getRepository().add(model);

    final User actual = getRepository().setEnabled(model.getId(), false);

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link IUserRepository#setEnabled(java.util.UUID, boolean)}.
   */
  @Test
  public void testSetEnabledNotFound() {
    assertNull(getRepository().setEnabled(RANDOM_ID, false));
  }

  /**
   * Test method for {@link IUserRepository#setVerified(java.util.UUID, boolean)}.
   */
  @Test
  public void testSetVerified() {
    final User model = User.builder(USERNAME, EMAIL).build();
    getRepository().add(model);

    final User actual = getRepository().setVerified(model.getId(), false);

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link IUserRepository#setVerified(java.util.UUID, boolean)}.
   */
  @Test
  public void testSetVerifiedNotFound() {
    assertNull(getRepository().setVerified(RANDOM_ID, false));
  }

  /**
   * Test method for {@link IUserRepository#exists(UUID, String, String)}.
   */
  @Test
  public void testExistsUUIDStringString() {
    final boolean expected = true;
    final User model = User.builder(USERNAME, EMAIL).build();
    final List<User> models = new ArrayList<>(1);
    models.add(model);
    getRepository().save(models);

    final boolean actual =
        getRepository().exists(model.getId(), model.getUsername(), model.getEmail());

    assertThat(actual, is(expected));
  }

  /**
   * Test method for {@link IUserRepository#exists(UUID, String, String)}.
   */
  @Test
  public void testExistsUUIDStringStringNotFound() {
    final boolean expected = false;

    final boolean actual = getRepository().exists(RANDOM_ID, USERNAME, EMAIL);

    assertThat(actual, is(expected));
  }

}
