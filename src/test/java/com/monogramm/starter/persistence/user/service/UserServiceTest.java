/*
 * Creation by madmath03 the 2017-09-02.
 */

package com.monogramm.starter.persistence.user.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.monogramm.starter.dto.user.RegistrationDto;
import com.monogramm.starter.dto.user.UserDto;
import com.monogramm.starter.persistence.AbstractGenericServiceTest;
import com.monogramm.starter.persistence.EntityNotFoundException;
import com.monogramm.starter.persistence.parameter.dao.IParameterRepository;
import com.monogramm.starter.persistence.parameter.entity.Parameter;
import com.monogramm.starter.persistence.parameter.exception.ParameterNotFoundException;
import com.monogramm.starter.persistence.role.dao.IRoleRepository;
import com.monogramm.starter.persistence.role.exception.RoleNotFoundException;
import com.monogramm.starter.persistence.user.dao.IUserRepository;
import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.persistence.user.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 * {@link UserService} Unit Test.
 * 
 * @author madmath03
 */
public class UserServiceTest extends AbstractGenericServiceTest<User, UserDto, UserService> {

  private static final String USERNAME = "Foo";
  private static final String EMAIL = "foo@email.com";
  private static final char[] PASSWORD = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};

  private IRoleRepository roleDao;

  private IParameterRepository parameterDao;

  private char[] password;

  /**
   * @throws java.lang.Exception if the test setup crashes.
   */
  @Before
  public void setUp() throws Exception {
    roleDao = mock(IRoleRepository.class);
    parameterDao = mock(IParameterRepository.class);
    password = PASSWORD.clone();

    super.setUp();
  }

  /**
   * @throws java.lang.Exception if the test cleanup crashes.
   */
  @After
  public void tearDown() throws Exception {
    super.tearDown();
    Mockito.reset(roleDao);
    Mockito.reset(parameterDao);
  }

  @Override
  protected UserService buildTestService() {
    return new UserService(getMockRepository(), roleDao, parameterDao,
        getMockAuthenticationFacade());
  }

  @Override
  protected IUserRepository buildMockRepository() {
    return getMockUserRepository();
  }

  @Override
  public IUserRepository getMockRepository() {
    return (IUserRepository) super.getMockRepository();
  }

  @Override
  protected User buildTestEntity() {
    return User.builder(USERNAME, EMAIL).id(ID).build();
  }

  @Override
  protected EntityNotFoundException buildEntityNotFoundException() {
    return new UserNotFoundException();
  }

  @Override
  @Test
  public void testExists() {
    final User model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getUsername(), model.getEmail()))
        .thenReturn(true);

    assertTrue(getService().exists(model));

    verify(getMockRepository(), times(1)).exists(model.getId(), model.getUsername(),
        model.getEmail());
    verifyNoMoreInteractions(getMockRepository());
  }

  @Override
  @Test
  public void testExistsNotFound() {
    final User model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getUsername(), model.getEmail()))
        .thenReturn(false);

    assertFalse(getService().exists(model));

    verify(getMockRepository(), times(1)).exists(model.getId(), model.getUsername(),
        model.getEmail());
    verifyNoMoreInteractions(getMockRepository());
  }

  /**
   * Test method for {@link UserService#add(com.monogramm.starter.persistence.user.entity.User)}.
   */
  @Override
  @Test
  public void testAdd() {
    final User model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getUsername(), model.getEmail()))
        .thenReturn(false);

    getService().add(model);

    ArgumentCaptor<User> userArgument = ArgumentCaptor.forClass(User.class);
    verify(getMockRepository(), times(1)).exists(model.getId(), model.getUsername(),
        model.getEmail());
    verify(getMockRepository(), times(1)).add(userArgument.capture());
    verifyNoMoreInteractions(getMockRepository());

    final User actual = userArgument.getValue();

    assertThat(actual.getUsername(), is(model.getUsername()));
    assertThat(actual.getEmail(), is(model.getEmail()));
  }

  /**
   * Test method for {@link UserService#add(com.monogramm.starter.persistence.user.entity.User)}.
   */
  @Override
  @Test
  public void testAddAlreadyExists() {
    final User model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getUsername(), model.getEmail()))
        .thenReturn(true);

    getService().add(model);

    verify(getMockRepository(), times(1)).exists(model.getId(), model.getUsername(),
        model.getEmail());
    verifyNoMoreInteractions(getMockRepository());
  }

  /**
   * Test method for
   * {@link UserService#UserService(IUserRepository, IRoleRepository, IParameterRepository, com.monogramm.starter.config.security.IAuthenticationFacade)}.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testUserServiceIRoleRepositoryNullNull() {
    new UserService(getMockRepository(), null, null, getMockAuthenticationFacade());
  }

  /**
   * Test method for
   * {@link UserService#UserService(IUserRepository, IRoleRepository, IParameterRepository, com.monogramm.starter.config.security.IAuthenticationFacade)}.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testUserServiceIRoleRepositoryRoleRepositoryNull() {
    new UserService(getMockRepository(), roleDao, null, getMockAuthenticationFacade());
  }

  /**
   * Test method for {@link UserService#getRoleRepository()}.
   */
  @Test
  public void testGetRoleRepository() {
    assertNotNull(getService().getRoleRepository());
    assertEquals(roleDao, getService().getRoleRepository());
  }

  /**
   * Test method for {@link UserService#getParameterRepository()}.
   */
  @Test
  public void testGetParameterRepository() {
    assertNotNull(getService().getParameterRepository());
    assertEquals(parameterDao, getService().getParameterRepository());
  }

  /**
   * Test method for {@link UserService#findAllContainingUsername(java.lang.String)}.
   */
  @Test
  public void testFindAllByUsername() {
    final List<User> models = new ArrayList<>();
    when(getMockRepository().findAllContainingUsernameIgnoreCase(USERNAME)).thenReturn(models);

    final List<User> actual = getService().findAllContainingUsername(USERNAME);

    verify(getMockRepository(), times(1)).findAllContainingUsernameIgnoreCase(USERNAME);
    verifyNoMoreInteractions(getMockRepository());

    assertThat(actual, is(models));
  }

  /**
   * Test method for {@link UserService#findAllContainingEmail(java.lang.String)}.
   */
  @Test
  public void testFindAllByEmail() {
    final List<User> models = new ArrayList<>();
    when(getMockRepository().findAllContainingEmailIgnoreCase(EMAIL)).thenReturn(models);

    final List<User> actual = getService().findAllContainingEmail(EMAIL);

    verify(getMockRepository(), times(1)).findAllContainingEmailIgnoreCase(EMAIL);
    verifyNoMoreInteractions(getMockRepository());

    assertThat(actual, is(models));
  }

  /**
   * Test method for
   * {@link UserService#findAllContainingUsernameOrEmail(java.lang.String, java.lang.String)}.
   */
  @Test
  public void testFindAllByUsernameOrEmail() {
    final List<User> models = new ArrayList<>();
    when(getMockRepository().findAllContainingUsernameOrEmailIgnoreCase(USERNAME, EMAIL))
        .thenReturn(models);

    final List<User> actual = getService().findAllContainingUsernameOrEmail(USERNAME, EMAIL);

    verify(getMockRepository(), times(1)).findAllContainingUsernameOrEmailIgnoreCase(USERNAME,
        EMAIL);
    verifyNoMoreInteractions(getMockRepository());

    assertThat(actual, is(models));
  }

  /**
   * Test method for {@link UserService#findByUsername(String)}.
   * 
   * @throws UserNotFoundException if no user matches the username in the repository.
   */
  @Test
  public void testFindByUsername() {
    final User model = this.buildTestEntity();

    when(getMockRepository().findByUsernameIgnoreCase(USERNAME)).thenReturn(model);

    final User actual = getService().findByUsername(USERNAME);

    verify(getMockRepository(), times(1)).findByUsernameIgnoreCase(USERNAME);
    verifyNoMoreInteractions(getMockRepository());

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link UserService#findByUsername(String)}.
   * 
   * @throws UserNotFoundException if no user matches the username in the repository.
   */
  @Test
  public void testFindByUsernameNotFound() {
    when(getMockRepository().findByUsernameIgnoreCase(USERNAME)).thenReturn(null);

    final User actual = getService().findByUsername(USERNAME);

    verify(getMockRepository(), times(1)).findByUsernameIgnoreCase(USERNAME);
    verifyNoMoreInteractions(getMockRepository());

    assertNull(actual);
  }

  /**
   * Test method for {@link UserService#findByUsername(String)}.
   * 
   * @throws UserNotFoundException if no user matches the username in the repository.
   */
  @Test(expected = UserNotFoundException.class)
  public void testFindByUsernameNotFoundException() {
    when(getMockRepository().findByUsernameIgnoreCase(USERNAME))
        .thenThrow(new UserNotFoundException());

    getService().findByUsername(USERNAME);
  }

  /**
   * Test method for {@link UserService#findByEmail(String)}.
   * 
   * @throws UserNotFoundException if no user matches the email in the repository.
   */
  @Test
  public void testFindByEmail() {
    final User model = this.buildTestEntity();

    when(getMockRepository().findByEmailIgnoreCase(EMAIL)).thenReturn(model);

    final User actual = getService().findByEmail(EMAIL);

    verify(getMockRepository(), times(1)).findByEmailIgnoreCase(EMAIL);
    verifyNoMoreInteractions(getMockRepository());

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link UserService#findByEmail(String)}.
   * 
   * @throws UserNotFoundException if no user matches the email in the repository.
   */
  @Test
  public void testFindByEmailNotFound() {
    when(getMockRepository().findByEmailIgnoreCase(EMAIL)).thenReturn(null);

    final User actual = getService().findByEmail(EMAIL);

    verify(getMockRepository(), times(1)).findByEmailIgnoreCase(EMAIL);
    verifyNoMoreInteractions(getMockRepository());

    assertNull(actual);
  }

  /**
   * Test method for {@link UserService#findByEmail(String)}.
   * 
   * @throws UserNotFoundException if no user matches the email in the repository.
   */
  @Test(expected = UserNotFoundException.class)
  public void testFindByEmailNotFoundException() {
    when(getMockRepository().findByEmailIgnoreCase(EMAIL)).thenThrow(new UserNotFoundException());

    getService().findByEmail(EMAIL);
  }

  /**
   * Test method for {@link UserService#findByUsernameOrEmail(String, String)}.
   * 
   * @throws UserNotFoundException if no user matches the username or email in the repository.
   */
  @Test
  public void testFindByUsernameOrEmail() {
    final User model = this.buildTestEntity();

    when(getMockRepository().findByUsernameOrEmailIgnoreCase(USERNAME, EMAIL)).thenReturn(model);

    final User actual = getService().findByUsernameOrEmail(USERNAME, EMAIL);

    verify(getMockRepository(), times(1)).findByUsernameOrEmailIgnoreCase(USERNAME, EMAIL);
    verifyNoMoreInteractions(getMockRepository());

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link UserService#findByUsernameOrEmail(String, String)}.
   * 
   * @throws UserNotFoundException if no user matches the username or email in the repository.
   */
  @Test
  public void testFindByUsernameOrEmailNotFound() {
    when(getMockRepository().findByUsernameOrEmailIgnoreCase(USERNAME, EMAIL)).thenReturn(null);

    final User actual = getService().findByUsernameOrEmail(USERNAME, EMAIL);

    verify(getMockRepository(), times(1)).findByUsernameOrEmailIgnoreCase(USERNAME, EMAIL);
    verifyNoMoreInteractions(getMockRepository());

    assertNull(actual);
  }

  /**
   * Test method for {@link UserService#findByUsernameOrEmail(String, String)}.
   * 
   * @throws UserNotFoundException if no user matches the email in the repository.
   */
  @Test
  public void testFindByUsernameOrEmailNotFoundException() {
    when(getMockRepository().findByUsernameOrEmailIgnoreCase(USERNAME, EMAIL))
        .thenThrow(new UserNotFoundException());

    final User actual = getService().findByUsernameOrEmail(USERNAME, EMAIL);

    verify(getMockRepository(), times(1)).findByUsernameOrEmailIgnoreCase(USERNAME, EMAIL);
    verifyNoMoreInteractions(getMockRepository());

    assertNull(actual);
  }

  /**
   * Test method for {@link UserService#update(com.monogramm.starter.persistence.user.entity.User)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test(expected = UserNotFoundException.class)
  public void testUpdateNotFoundException() {
    final User model = this.buildTestEntity();

    when(getMockRepository().update(model)).thenThrow(new UserNotFoundException());

    getService().update(model);
  }



  /**
   * Test method for {@link UserService#setPassword(UUID, char[])}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test
  public void testSetPassword() {
    final User model = this.buildTestEntity();

    when(getMockRepository().setPassword(ID, password)).thenReturn(model);

    final User actual = getService().setPassword(ID, password);

    verify(getMockRepository(), times(1)).setPassword(ID, password);
    verifyNoMoreInteractions(getMockRepository());

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link UserService#setPassword(UUID, char[])}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test(expected = UserNotFoundException.class)
  public void testSetPasswordNotFound() {
    when(getMockRepository().setPassword(ID, password)).thenReturn(null);

    getService().setPassword(ID, password);
  }

  /**
   * Test method for {@link UserService#setPassword(UUID, char[])}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test(expected = UserNotFoundException.class)
  public void testSetPasswordNotFoundException() {
    when(getMockRepository().setPassword(ID, password)).thenThrow(new UserNotFoundException());

    getService().setPassword(ID, password);
  }


  /**
   * Test method for {@link UserService#setPasswordByOwner(UUID, char[], UUID)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test
  public void testSetPasswordByOwner() {
    final User model = this.buildTestEntity();
    final UUID ownerId = UUID.randomUUID();
    final User owner = User.builder().id(ownerId).build();

    when(getMockRepository().setPasswordByOwner(ID, password, owner)).thenReturn(model);

    final User actual = getService().setPasswordByOwner(ID, password, ownerId);

    verify(getMockRepository(), times(1)).setPasswordByOwner(ID, password, owner);
    verifyNoMoreInteractions(getMockRepository());

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link UserService#setPasswordByOwner(UUID, char[], UUID)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test(expected = UserNotFoundException.class)
  public void testSetPasswordByOwnerNotFound() {
    final UUID ownerId = UUID.randomUUID();
    final User owner = User.builder().id(ownerId).build();

    when(getMockRepository().setPasswordByOwner(ID, password, owner)).thenReturn(null);

    getService().setPasswordByOwner(ID, password, ownerId);
  }

  /**
   * Test method for {@link UserService#setPasswordByOwner(UUID, char[], UUID)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test(expected = UserNotFoundException.class)
  public void testSetPasswordByOwnerNotFoundException() {
    final UUID ownerId = UUID.randomUUID();
    final User owner = User.builder().id(ownerId).build();

    when(getMockRepository().setPasswordByOwner(ID, password, owner))
        .thenThrow(new UserNotFoundException());

    getService().setPasswordByOwner(ID, password, ownerId);
  }



  /**
   * Test method for {@link UserService#setEnabled(UUID, boolean)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test
  public void testSetEnabled() {
    final User model = this.buildTestEntity();

    when(getMockRepository().setEnabled(ID, false)).thenReturn(model);

    final User actual = getService().setEnabled(ID, false);

    verify(getMockRepository(), times(1)).setEnabled(ID, false);
    verifyNoMoreInteractions(getMockRepository());

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link UserService#setEnabled(UUID, boolean)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test(expected = UserNotFoundException.class)
  public void testSetEnabledNotFound() {
    when(getMockRepository().setEnabled(ID, false)).thenReturn(null);

    getService().setEnabled(ID, false);
  }

  /**
   * Test method for {@link UserService#setEnabled(UUID, boolean)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test(expected = UserNotFoundException.class)
  public void testSetEnabledNotFoundException() {
    when(getMockRepository().setEnabled(ID, false)).thenThrow(new UserNotFoundException());

    getService().setEnabled(ID, false);
  }


  /**
   * Test method for {@link UserService#setEnabled(UUID, boolean)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test
  public void testSetEnabledByOwner() {
    final User model = this.buildTestEntity();
    final UUID ownerId = UUID.randomUUID();
    final User owner = User.builder().id(ownerId).build();

    when(getMockRepository().setEnabledByOwner(ID, false, owner)).thenReturn(model);

    final User actual = getService().setEnabledByOwner(ID, false, ownerId);

    verify(getMockRepository(), times(1)).setEnabledByOwner(ID, false, owner);
    verifyNoMoreInteractions(getMockRepository());

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link UserService#setEnabled(UUID, boolean)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test(expected = UserNotFoundException.class)
  public void testSetEnabledByOwnerNotFound() {
    final UUID ownerId = UUID.randomUUID();
    final User owner = User.builder().id(ownerId).build();

    when(getMockRepository().setEnabledByOwner(ID, false, owner)).thenReturn(null);

    getService().setEnabledByOwner(ID, false, ownerId);
  }

  /**
   * Test method for {@link UserService#setEnabled(UUID, boolean)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test(expected = UserNotFoundException.class)
  public void testSetEnabledByOwnerNotFoundException() {
    final UUID ownerId = UUID.randomUUID();
    final User owner = User.builder().id(ownerId).build();

    when(getMockRepository().setEnabledByOwner(ID, false, owner))
        .thenThrow(new UserNotFoundException());

    getService().setEnabledByOwner(ID, false, ownerId);
  }



  /**
   * Test method for {@link UserService#enable(UUID)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test
  public void testEnable() {
    final User model = this.buildTestEntity();

    when(getMockRepository().setEnabled(ID, true)).thenReturn(model);

    final User actual = getService().enable(ID);

    verify(getMockRepository(), times(1)).setEnabled(ID, true);
    verifyNoMoreInteractions(getMockRepository());

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link UserService#enable(UUID)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test(expected = UserNotFoundException.class)
  public void testEnableNotFound() {
    when(getMockRepository().setEnabled(ID, true)).thenReturn(null);

    getService().enable(ID);
  }

  /**
   * Test method for {@link UserService#enable(UUID)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test(expected = UserNotFoundException.class)
  public void testEnableNotFoundException() {
    when(getMockRepository().setEnabled(ID, true)).thenThrow(new UserNotFoundException());

    getService().enable(ID);
  }


  /**
   * Test method for {@link UserService#enableByOwner(UUID, UUID)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test
  public void testEnableByOwner() {
    final User model = this.buildTestEntity();
    final UUID ownerId = UUID.randomUUID();
    final User owner = User.builder().id(ownerId).build();

    when(getMockRepository().setEnabledByOwner(ID, true, owner)).thenReturn(model);

    final User actual = getService().enableByOwner(ID, ownerId);

    verify(getMockRepository(), times(1)).setEnabledByOwner(ID, true, owner);
    verifyNoMoreInteractions(getMockRepository());

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link UserService#enableByOwner(UUID, UUID)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test(expected = UserNotFoundException.class)
  public void testEnableByOwnerNotFound() {
    final UUID ownerId = UUID.randomUUID();
    final User owner = User.builder().id(ownerId).build();

    when(getMockRepository().setEnabledByOwner(ID, true, owner)).thenReturn(null);

    getService().enableByOwner(ID, ownerId);
  }

  /**
   * Test method for {@link UserService#enableByOwner(UUID, UUID)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test(expected = UserNotFoundException.class)
  public void testEnableByOwnerNotFoundException() {
    final UUID ownerId = UUID.randomUUID();
    final User owner = User.builder().id(ownerId).build();

    when(getMockRepository().setEnabledByOwner(ID, true, owner))
        .thenThrow(new UserNotFoundException());

    getService().enableByOwner(ID, ownerId);
  }



  /**
   * Test method for {@link UserService#register(com.monogramm.starter.dto.user.RegistrationDto)}.
   * 
   * @throws RoleNotFoundException if the default role is not found.
   */
  @Test
  public void testRegister() {
    final RegistrationDto model = new RegistrationDto();
    model.setUsername(USERNAME);
    model.setEmail(EMAIL);
    model.setPassword(PASSWORD);
    model.setMatchingPassword(PASSWORD);

    when(getMockRepository().exists(null, model.getUsername(), model.getEmail())).thenReturn(false);
    when(parameterDao.findByNameIgnoreCase("DEFAULT_ROLE"))
        .thenThrow(new ParameterNotFoundException("TEST"));
    when(roleDao.findByNameIgnoreCase("User")).thenReturn(null);

    getService().register(model);

    ArgumentCaptor<User> userArgument = ArgumentCaptor.forClass(User.class);
    verify(getMockRepository(), times(1)).exists(null, model.getUsername(), model.getEmail());
    verify(getMockRepository(), times(1)).add(userArgument.capture());
    verifyNoMoreInteractions(getMockRepository());

    verify(parameterDao, times(1)).findByNameIgnoreCase("DEFAULT_ROLE");
    verifyNoMoreInteractions(parameterDao);

    verify(roleDao, times(1)).findByNameIgnoreCase("User");
    verifyNoMoreInteractions(roleDao);

    final User actual = userArgument.getValue();

    assertNull(actual.getId());
    assertThat(actual.getUsername(), is(model.getUsername()));
    assertThat(actual.getEmail(), is(model.getEmail()));
  }

  /**
   * Test method for {@link UserService#register(com.monogramm.starter.dto.user.RegistrationDto)}.
   * 
   * @throws RoleNotFoundException if the default role is not found.
   */
  @Test
  public void testRegisterAlreadyExists() {
    final RegistrationDto model = new RegistrationDto();
    model.setUsername(USERNAME);
    model.setEmail(EMAIL);
    model.setPassword(PASSWORD);
    model.setMatchingPassword(PASSWORD);

    when(getMockRepository().exists(null, model.getUsername(), model.getEmail())).thenReturn(true);
    when(parameterDao.findByNameIgnoreCase("DEFAULT_ROLE")).thenReturn(null);
    when(roleDao.findByNameIgnoreCase("User")).thenReturn(null);

    getService().register(model);

    verify(getMockRepository(), times(1)).exists(null, model.getUsername(), model.getEmail());
    verifyNoMoreInteractions(getMockRepository());
  }

  /**
   * Test method for {@link UserService#register(com.monogramm.starter.dto.user.RegistrationDto)}.
   * 
   * @throws RoleNotFoundException if the default role is not found.
   */
  @Test(expected = RoleNotFoundException.class)
  public void testRegisterRoleNotFoundException() {
    final RegistrationDto model = new RegistrationDto();
    model.setUsername(USERNAME);
    model.setEmail(EMAIL);
    model.setPassword(PASSWORD);
    model.setMatchingPassword(PASSWORD);

    when(getMockRepository().exists(null, model.getUsername(), model.getEmail())).thenReturn(false);
    when(parameterDao.findByNameIgnoreCase("DEFAULT_ROLE")).thenReturn(Parameter.builder().build());
    when(roleDao.findByNameIgnoreCase(null)).thenThrow(new RoleNotFoundException());

    getService().register(model);
  }



  /**
   * Test method for {@link UserService#verify(UUID)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test
  public void testVerify() {
    final User model = this.buildTestEntity();

    when(getMockRepository().setVerified(ID, true)).thenReturn(model);

    final User actual = getService().verify(ID);

    verify(getMockRepository(), times(1)).setVerified(ID, true);
    verifyNoMoreInteractions(getMockRepository());

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link UserService#verify(UUID)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test(expected = UserNotFoundException.class)
  public void testVerifyNotFound() {
    when(getMockRepository().setVerified(ID, true)).thenReturn(null);

    getService().verify(ID);
  }

  /**
   * Test method for {@link UserService#verify(UUID)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test(expected = UserNotFoundException.class)
  public void testVerifyNotFoundException() {
    when(getMockRepository().setVerified(ID, true)).thenThrow(new UserNotFoundException());

    getService().verify(ID);
  }


  /**
   * Test method for {@link UserService#verifyByOwner(UUID, UUID)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test
  public void testVerifyByOwner() {
    final User model = this.buildTestEntity();
    final UUID ownerId = UUID.randomUUID();
    final User owner = User.builder().id(ownerId).build();

    when(getMockRepository().setVerifiedByOwner(ID, true, owner)).thenReturn(model);

    final User actual = getService().verifyByOwner(ID, ownerId);

    verify(getMockRepository(), times(1)).setVerifiedByOwner(ID, true, owner);
    verifyNoMoreInteractions(getMockRepository());

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link UserService#verifyByOwner(UUID, UUID)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test(expected = UserNotFoundException.class)
  public void testVerifyByOwnerNotFound() {
    final UUID ownerId = UUID.randomUUID();
    final User owner = User.builder().id(ownerId).build();

    when(getMockRepository().setVerifiedByOwner(ID, true, owner)).thenReturn(null);

    getService().verifyByOwner(ID, ownerId);
  }

  /**
   * Test method for {@link UserService#verifyByOwner(UUID, UUID)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test(expected = UserNotFoundException.class)
  public void testVerifyByOwnerNotFoundException() {
    final UUID ownerId = UUID.randomUUID();
    final User owner = User.builder().id(ownerId).build();

    when(getMockRepository().setVerifiedByOwner(ID, true, owner))
        .thenThrow(new UserNotFoundException());

    getService().verifyByOwner(ID, ownerId);
  }

}
