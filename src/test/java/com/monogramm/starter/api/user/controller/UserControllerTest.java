/*
 * Creation by madmath03 the 2017-09-04.
 */

package com.monogramm.starter.api.user.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.monogramm.starter.api.AbstractGenericController;
import com.monogramm.starter.api.AbstractGenericControllerTest;
import com.monogramm.starter.dto.user.PasswordResetDto;
import com.monogramm.starter.dto.user.RegistrationDto;
import com.monogramm.starter.dto.user.UserDto;
import com.monogramm.starter.persistence.AbstractGenericBridge;
import com.monogramm.starter.persistence.EntityNotFoundException;
import com.monogramm.starter.persistence.role.exception.RoleNotFoundException;
import com.monogramm.starter.persistence.user.entity.PasswordResetToken;
import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.persistence.user.entity.VerificationToken;
import com.monogramm.starter.persistence.user.exception.PasswordResetTokenNotFoundException;
import com.monogramm.starter.persistence.user.exception.UserNotFoundException;
import com.monogramm.starter.persistence.user.exception.VerificationTokenNotFoundException;
import com.monogramm.starter.persistence.user.service.IPasswordResetTokenService;
import com.monogramm.starter.persistence.user.service.IUserService;
import com.monogramm.starter.persistence.user.service.IVerificationTokenService;
import com.monogramm.starter.persistence.user.service.UserBridge;
import com.monogramm.starter.utils.validation.PasswordConfirmationDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.context.request.WebRequest;

/**
 * {@link UserController} Unit Test.
 * 
 * @author madmath03
 */
public class UserControllerTest extends AbstractGenericControllerTest<User, UserDto> {

  private static final UUID ID = UUID.randomUUID();
  private static final String USERNAME = "Foo";
  private static final String EMAIL = "foo@email.com";
  private static final char[] PASSWORD = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
  private static final String TOKEN = "Foo";

  private char[] password;

  private ApplicationEventPublisher eventPublisher;
  private IVerificationTokenService verificationService;
  private IPasswordResetTokenService passwordResetTokenService;

  private WebRequest request;

  /**
   * Setup test case.
   * 
   * @throws java.lang.Exception if the test setup crashes.
   */
  @Before
  public void setUp() throws Exception {
    password = PASSWORD.clone();

    eventPublisher = mock(ApplicationEventPublisher.class);
    assertNotNull(eventPublisher);
    verificationService = mock(IVerificationTokenService.class);
    assertNotNull(verificationService);
    passwordResetTokenService = mock(IPasswordResetTokenService.class);
    assertNotNull(passwordResetTokenService);

    request = mock(WebRequest.class);
    assertNotNull(request);

    super.setUp();
  }

  /**
   * Tear down test case.
   * 
   * @throws java.lang.Exception if the test cleanup crashes.
   */
  @After
  public void tearDown() throws Exception {
    Mockito.reset(getMockService());
    this.setBridge(null);

    this.password = null;

    Mockito.reset(eventPublisher);
    Mockito.reset(verificationService);
    Mockito.reset(passwordResetTokenService);

    Mockito.reset(request);

    super.tearDown();
  }

  @Override
  protected IUserService getMockService() {
    return (IUserService) super.getMockService();
  }

  @Override
  protected UserController getController() {
    return (UserController) super.getController();
  }

  @Override
  protected IUserService buildTestService() {
    return mock(IUserService.class);
  }

  @Override
  protected Authentication buildMockAuthentication() {
    return mock(Authentication.class);
  }

  @Override
  protected AbstractGenericController<User, UserDto> buildTestController() {
    return new UserController(getMockService(), eventPublisher, verificationService,
        passwordResetTokenService);
  }

  @Override
  protected AbstractGenericBridge<User, UserDto> buildTestBridge() {
    return new UserBridge();
  }

  @Override
  protected User buildTestEntity() {
    return User.builder(USERNAME, EMAIL).id(ID).build();
  }

  @Override
  protected Class<User> getEntityClass() {
    return User.class;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected UserNotFoundException buildTestEntityNotFound() {
    return new UserNotFoundException();
  }

  /**
   * Test method for
   * {@link UserController#UserController(IUserService, ApplicationEventPublisher, IVerificationTokenService, IPasswordResetTokenService)}.
   */
  @Test
  public void testUserController() {
    assertNotNull(getController());
  }

  /**
   * Test method for {@link UserController#getUserByUsernameOrEmail(String, String)}.
   */
  @Test
  public void testGetUserByUsernameOrEmail() {
    final User model = this.buildTestEntity();
    final UserDto dto = getBridge().toDto(model);
    final ResponseEntity<UserDto> expectedResponse = new ResponseEntity<>(dto, HttpStatus.OK);

    when(this.getMockService().findByUsernameOrEmail(model.getUsername(), model.getEmail()))
        .thenReturn(model);
    when(this.getMockService().toDto(model)).thenReturn(dto);

    final ResponseEntity<UserDto> actual =
        getController().getUserByUsernameOrEmail(model.getUsername(), model.getEmail());

    verify(this.getMockService(), times(1)).findByUsernameOrEmail(model.getUsername(),
        model.getEmail());
    verify(this.getMockService(), times(1)).toDto(model);
    verifyNoMoreInteractions(this.getMockService());

    assertThat(actual, is(expectedResponse));
  }

  /**
   * Test method for {@link UserController#getUserByUsernameOrEmail(String, String)}.
   */
  @Test
  public void testGeUserByUsernameOrEmailNotFound() {
    final User model = null;
    final UserDto dto = null;
    final ResponseEntity<UserDto> expectedResponse =
        new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);

    when(this.getMockService().findByUsernameOrEmail(null, null)).thenReturn(model);

    final ResponseEntity<UserDto> actual = getController().getUserByUsernameOrEmail(null, null);

    verify(this.getMockService(), times(1)).findByUsernameOrEmail(null, null);
    verifyNoMoreInteractions(this.getMockService());

    assertThat(actual, is(expectedResponse));
  }

  /**
   * Test method for {@link UserController#getUserByUsernameOrEmail(String, String)}.
   */
  @Test
  public void testGeUserByUsernameOrEmailIllegal() {
    final UserDto dto = null;
    final ResponseEntity<UserDto> expectedResponse =
        new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);

    when(this.getMockService().findByUsernameOrEmail("dummy_name", "this_is_not_an_email"))
        .thenReturn(null);

    final ResponseEntity<UserDto> actual =
        getController().getUserByUsernameOrEmail("dummy_name", "this_is_not_an_email");

    verify(this.getMockService(), times(1)).findByUsernameOrEmail("dummy_name",
        "this_is_not_an_email");
    verifyNoMoreInteractions(this.getMockService());

    assertThat(actual, is(expectedResponse));
  }

  /**
   * Test method for {@link UserController#resetPassword(java.lang.String)}.
   */
  @Test
  public void testResetPasswordString() {
    final User model = User.builder(USERNAME, EMAIL).id(ID).build();
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);

    when(getMockService().findByEmail(EMAIL)).thenReturn(model);

    final ResponseEntity<Void> actual = getController().resetPassword(EMAIL, request);

    verify(getMockService(), times(1)).findByEmail(EMAIL);
    verifyNoMoreInteractions(getMockService());

    assertThat(actual.getStatusCode(), is(expectedResponse.getStatusCode()));
    assertNull(expectedResponse.getBody());

    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link UserController#resetPassword(java.lang.String)}.
   */
  @Test
  public void testResetPasswordStringNotFound() {
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);

    when(getMockService().findByEmail(EMAIL)).thenReturn(null);

    final ResponseEntity<Void> actual = getController().resetPassword(EMAIL, request);

    verify(getMockService(), times(1)).findByEmail(EMAIL);
    verifyNoMoreInteractions(getMockService());

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link UserController#resetPassword(java.lang.String)}.
   */
  @Test
  public void testResetPasswordStringNotFoundException() {
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);

    when(getMockService().findByEmail(EMAIL)).thenThrow(new UserNotFoundException());

    final ResponseEntity<Void> actual = getController().resetPassword(EMAIL, request);

    verify(getMockService(), times(1)).findByEmail(EMAIL);
    verifyNoMoreInteractions(getMockService());

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link UserController#resetPassword(java.lang.String)}.
   * 
   * @throws PasswordResetTokenNotFoundException if password reset not found.
   * @throws UserNotFoundException if user not found.
   */
  @Test
  public void testResetPasswordPasswordResetDto() {
    final User model = User.builder(USERNAME, EMAIL).id(ID).build();
    final PasswordResetToken passwordResetToken =
        new PasswordResetToken(TOKEN, new Date(System.currentTimeMillis() + 1440 * 60_000));
    final PasswordResetDto passwordReset =
        new PasswordResetDto(EMAIL, TOKEN, PASSWORD.clone(), PASSWORD.clone());
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);

    when(getMockService().findByEmail(EMAIL)).thenReturn(model);
    when(passwordResetTokenService.findByUserAndCode(model, TOKEN)).thenReturn(passwordResetToken);
    when(getMockService().setPassword(model.getId(), passwordReset.getPassword()))
        .thenReturn(model);

    final ResponseEntity<Void> actual = getController().resetPassword(passwordReset);

    verify(getMockService(), times(1)).findByEmail(EMAIL);
    verify(getMockService(), times(1)).setPassword(model.getId(), passwordReset.getPassword());
    verifyNoMoreInteractions(getMockService());

    verify(passwordResetTokenService, times(1)).findByUserAndCode(model, TOKEN);
    verify(passwordResetTokenService, times(1)).update(passwordResetToken);
    verifyNoMoreInteractions(passwordResetTokenService);

    assertThat(actual.getStatusCode(), is(expectedResponse.getStatusCode()));
    assertNull(expectedResponse.getBody());

    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link UserController#resetPassword(java.lang.String)}.
   * 
   * @throws PasswordResetTokenNotFoundException if password reset not found.
   * @throws UserNotFoundException if user not found.
   */
  @Test
  public void testResetPasswordPasswordResetDtoOutOfDate() {
    final User model = User.builder(USERNAME, EMAIL).id(ID).build();
    final PasswordResetToken passwordResetToken =
        new PasswordResetToken(TOKEN, new Date(System.currentTimeMillis() - 1));
    final PasswordResetDto passwordReset =
        new PasswordResetDto(EMAIL, TOKEN, PASSWORD.clone(), PASSWORD.clone());
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);

    when(getMockService().findByEmail(EMAIL)).thenReturn(model);
    when(passwordResetTokenService.findByUserAndCode(model, TOKEN)).thenReturn(passwordResetToken);

    final ResponseEntity<Void> actual = getController().resetPassword(passwordReset);

    verify(getMockService(), times(1)).findByEmail(EMAIL);
    verifyNoMoreInteractions(getMockService());

    verify(passwordResetTokenService, times(1)).findByUserAndCode(model, TOKEN);
    verifyNoMoreInteractions(passwordResetTokenService);

    assertThat(actual.getStatusCode(), is(expectedResponse.getStatusCode()));
    assertNull(expectedResponse.getBody());

    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link UserController#resetPassword(java.lang.String)}.
   * 
   * @throws PasswordResetTokenNotFoundException if password reset not found.
   * @throws UserNotFoundException if user not found.
   */
  @Test
  public void testResetPasswordPasswordResetDtoPasswordUserNotFound() {
    final User model = User.builder(USERNAME, EMAIL).id(ID).build();
    final PasswordResetToken passwordResetToken =
        new PasswordResetToken(TOKEN, new Date(System.currentTimeMillis() + 1440 * 60_000));
    final PasswordResetDto passwordReset =
        new PasswordResetDto(EMAIL, TOKEN, PASSWORD.clone(), PASSWORD.clone());
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);

    when(getMockService().findByEmail(EMAIL)).thenReturn(model);
    when(passwordResetTokenService.findByUserAndCode(model, TOKEN)).thenReturn(passwordResetToken);
    when(getMockService().setPassword(model.getId(), passwordReset.getPassword())).thenReturn(null);

    final ResponseEntity<Void> actual = getController().resetPassword(passwordReset);

    verify(getMockService(), times(1)).findByEmail(EMAIL);
    verify(getMockService(), times(1)).setPassword(model.getId(), passwordReset.getPassword());
    verifyNoMoreInteractions(getMockService());

    verify(passwordResetTokenService, times(1)).findByUserAndCode(model, TOKEN);
    verifyNoMoreInteractions(passwordResetTokenService);

    assertThat(actual.getStatusCode(), is(expectedResponse.getStatusCode()));
    assertNull(expectedResponse.getBody());

    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link UserController#resetPassword(java.lang.String)}.
   */
  @Test
  public void testResetPasswordPasswordResetDtoUserNotFound() {
    final PasswordResetDto passwordReset =
        new PasswordResetDto(EMAIL, TOKEN, PASSWORD.clone(), PASSWORD.clone());
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);

    when(getMockService().findByEmail(EMAIL)).thenReturn(null);

    final ResponseEntity<Void> actual = getController().resetPassword(passwordReset);

    verify(getMockService(), times(1)).findByEmail(EMAIL);
    verifyNoMoreInteractions(getMockService());

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link UserController#resetPassword(java.lang.String)}.
   * 
   * @throws PasswordResetTokenNotFoundException if password reset not found.
   */
  @Test
  public void testResetPasswordPasswordResetDtoTokenNotFound() {
    final User model = User.builder(USERNAME, EMAIL).id(ID).build();
    final PasswordResetDto passwordReset =
        new PasswordResetDto(EMAIL, TOKEN, PASSWORD.clone(), PASSWORD.clone());
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);

    when(getMockService().findByEmail(EMAIL)).thenReturn(model);
    when(passwordResetTokenService.findByUserAndCode(model, TOKEN)).thenReturn(null);

    final ResponseEntity<Void> actual = getController().resetPassword(passwordReset);

    verify(getMockService(), times(1)).findByEmail(EMAIL);
    verifyNoMoreInteractions(getMockService());

    verify(passwordResetTokenService, times(1)).findByUserAndCode(model, TOKEN);
    verifyNoMoreInteractions(passwordResetTokenService);

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link UserController#resetPassword(java.lang.String)}.
   */
  @Test
  public void testResetPasswordPasswordResetDtoTokenNotFoundException() {
    final User model = User.builder(USERNAME, EMAIL).id(ID).build();
    final PasswordResetDto passwordReset =
        new PasswordResetDto(EMAIL, TOKEN, PASSWORD.clone(), PASSWORD.clone());
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);

    when(getMockService().findByEmail(EMAIL)).thenReturn(model);
    when(passwordResetTokenService.findByUserAndCode(model, TOKEN))
        .thenThrow(new PasswordResetTokenNotFoundException());

    final ResponseEntity<Void> actual = getController().resetPassword(passwordReset);

    verify(getMockService(), times(1)).findByEmail(EMAIL);
    verifyNoMoreInteractions(getMockService());

    verify(passwordResetTokenService, times(1)).findByUserAndCode(model, TOKEN);
    verifyNoMoreInteractions(passwordResetTokenService);

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }

  /**
   * Test method for
   * {@link UserController#changePassword(Authentication, String, PasswordConfirmationDto)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test
  public void testChangePassword() {
    final User model = User.builder(USERNAME, EMAIL).id(ID).build();
    final PasswordConfirmationDto dto = new PasswordConfirmationDto(password, password);
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);

    final String[] adminAuthorities = getController().getAdminAuthorities();
    assertNotNull(adminAuthorities);
    final Collection<GrantedAuthority> userAuthorities = new ArrayList<>(adminAuthorities.length);
    for (final String adminAuth : getController().getAdminAuthorities()) {
      userAuthorities.add(new SimpleGrantedAuthority(adminAuth));
    }

    when(getMockAuthentication().getAuthorities()).then(invocation -> userAuthorities);
    when(getMockAuthentication().getDetails()).thenReturn(null);
    when(getMockService().setPassword(ID, password)).thenReturn(model);

    final ResponseEntity<Void> actual =
        getController().changePassword(getMockAuthentication(), ID.toString(), dto);

    verify(getMockAuthentication(), times(1)).getAuthorities();
    verify(getMockAuthentication(), times(1)).getDetails();
    verifyNoMoreInteractions(getMockAuthentication());
    verify(getMockService(), times(1)).setPassword(ID, password);
    verify(getMockService(), times(1)).update(model);
    verifyNoMoreInteractions(getMockService());

    assertThat(actual.getStatusCode(), is(expectedResponse.getStatusCode()));

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }

  /**
   * Test method for
   * {@link UserController#changePassword(Authentication, String, PasswordConfirmationDto)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test
  public void testChangePasswordNotAdmin() {
    final User model = User.builder(USERNAME, EMAIL).id(ID).build();
    final PasswordConfirmationDto dto = new PasswordConfirmationDto(password, password);
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);
    final UUID principalId = null;

    when(getMockAuthentication().getAuthorities()).thenReturn(Collections.emptyList());
    when(getMockAuthentication().getDetails()).thenReturn(null);
    when(getMockService().setPasswordByOwner(ID, password, principalId)).thenReturn(model);

    final ResponseEntity<Void> actual =
        getController().changePassword(getMockAuthentication(), ID.toString(), dto);

    verify(getMockAuthentication(), times(1)).getAuthorities();
    verify(getMockAuthentication(), times(2)).getDetails();
    verifyNoMoreInteractions(getMockAuthentication());
    verify(getMockService(), times(1)).setPasswordByOwner(ID, password, principalId);
    verify(getMockService(), times(1)).update(model);
    verifyNoMoreInteractions(getMockService());

    assertThat(actual.getStatusCode(), is(expectedResponse.getStatusCode()));

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }

  /**
   * Test method for
   * {@link UserController#changePassword(Authentication, String, PasswordConfirmationDto)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test
  public void testChangePasswordDtoNull() {
    final PasswordConfirmationDto dto = null;
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    final ResponseEntity<Void> actual =
        getController().changePassword(getMockAuthentication(), ID.toString(), dto);

    verifyNoMoreInteractions(getMockAuthentication());
    verifyNoMoreInteractions(getMockService());

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }

  /**
   * Test method for
   * {@link UserController#changePassword(Authentication, String, PasswordConfirmationDto)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test
  public void testChangePasswordNotFound() {
    final PasswordConfirmationDto dto = new PasswordConfirmationDto(password, password);
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);

    final String[] adminAuthorities = getController().getAdminAuthorities();
    assertNotNull(adminAuthorities);
    final Collection<GrantedAuthority> userAuthorities = new ArrayList<>(adminAuthorities.length);
    for (final String adminAuth : getController().getAdminAuthorities()) {
      userAuthorities.add(new SimpleGrantedAuthority(adminAuth));
    }

    when(getMockAuthentication().getAuthorities()).then(invocation -> userAuthorities);
    when(getMockService().setPassword(ID, password)).thenReturn(null);

    final ResponseEntity<Void> actual =
        getController().changePassword(getMockAuthentication(), ID.toString(), dto);

    verify(getMockAuthentication(), times(1)).getAuthorities();
    verifyNoMoreInteractions(getMockAuthentication());
    verify(getMockService(), times(1)).setPassword(ID, password);
    verifyNoMoreInteractions(getMockService());

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }

  /**
   * Test method for
   * {@link UserController#changePassword(Authentication, String, PasswordConfirmationDto)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test
  public void testChangePasswordNotFoundException() {
    final PasswordConfirmationDto dto = new PasswordConfirmationDto(password, password);
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);

    final String[] adminAuthorities = getController().getAdminAuthorities();
    assertNotNull(adminAuthorities);
    final Collection<GrantedAuthority> userAuthorities = new ArrayList<>(adminAuthorities.length);
    for (final String adminAuth : getController().getAdminAuthorities()) {
      userAuthorities.add(new SimpleGrantedAuthority(adminAuth));
    }

    when(getMockAuthentication().getAuthorities()).then(invocation -> userAuthorities);
    when(getMockService().setPassword(ID, password)).thenThrow(new UserNotFoundException());

    final ResponseEntity<Void> actual =
        getController().changePassword(getMockAuthentication(), ID.toString(), dto);

    verify(getMockAuthentication(), times(1)).getAuthorities();
    verifyNoMoreInteractions(getMockAuthentication());
    verify(getMockService(), times(1)).setPassword(ID, password);
    verifyNoMoreInteractions(getMockService());

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }

  /**
   * Test method for
   * {@link UserController#changePassword(Authentication, String, PasswordConfirmationDto)}.
   */
  @Test
  public void testChangePasswordIdIllegal() {
    final PasswordConfirmationDto dto = new PasswordConfirmationDto(password, password);
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);

    final String[] adminAuthorities = getController().getAdminAuthorities();
    assertNotNull(adminAuthorities);
    final Collection<GrantedAuthority> userAuthorities = new ArrayList<>(adminAuthorities.length);
    for (final String adminAuth : getController().getAdminAuthorities()) {
      userAuthorities.add(new SimpleGrantedAuthority(adminAuth));
    }

    when(getMockAuthentication().getAuthorities()).then(invocation -> userAuthorities);

    final ResponseEntity<Void> actual =
        getController().changePassword(getMockAuthentication(), "this_is_not_a_UUID", dto);

    verify(getMockAuthentication(), times(1)).getAuthorities();
    verifyNoMoreInteractions(getMockAuthentication());
    verifyNoMoreInteractions(getMockService());

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }



  /**
   * Test method for {@link UserController#activate(Authentication, String, Boolean)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test
  public void testActivate() {
    final User model = User.builder(USERNAME, EMAIL).id(ID).build();
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);

    final String[] adminAuthorities = getController().getAdminAuthorities();
    assertNotNull(adminAuthorities);
    final Collection<GrantedAuthority> userAuthorities = new ArrayList<>(adminAuthorities.length);
    for (final String adminAuth : getController().getAdminAuthorities()) {
      userAuthorities.add(new SimpleGrantedAuthority(adminAuth));
    }

    when(getMockAuthentication().getAuthorities()).then(invocation -> userAuthorities);
    when(getMockAuthentication().getDetails()).thenReturn(null);
    when(getMockService().setEnabled(ID, false)).thenReturn(model);

    final ResponseEntity<Void> actual =
        getController().activate(getMockAuthentication(), ID.toString(), Boolean.FALSE);

    verify(getMockAuthentication(), times(1)).getAuthorities();
    verify(getMockAuthentication(), times(1)).getDetails();
    verifyNoMoreInteractions(getMockAuthentication());
    verify(getMockService(), times(1)).setEnabled(ID, false);
    verify(getMockService(), times(1)).update(model);
    verifyNoMoreInteractions(getMockService());

    assertThat(actual.getStatusCode(), is(expectedResponse.getStatusCode()));
    assertNull(expectedResponse.getBody());

    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link UserController#activate(Authentication, String, Boolean)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test
  public void testActivateNotAdmin() {
    final User model = User.builder(USERNAME, EMAIL).id(ID).build();
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);
    final UUID principalId = null;

    when(getMockAuthentication().getAuthorities()).thenReturn(Collections.emptyList());
    when(getMockAuthentication().getDetails()).thenReturn(null);
    when(getMockService().setEnabledByOwner(ID, false, principalId)).thenReturn(model);

    final ResponseEntity<Void> actual =
        getController().activate(getMockAuthentication(), ID.toString(), Boolean.FALSE);

    verify(getMockAuthentication(), times(1)).getAuthorities();
    verify(getMockAuthentication(), times(2)).getDetails();
    verifyNoMoreInteractions(getMockAuthentication());
    verify(getMockService(), times(1)).setEnabledByOwner(ID, false, principalId);
    verify(getMockService(), times(1)).update(model);
    verifyNoMoreInteractions(getMockService());

    assertThat(actual.getStatusCode(), is(expectedResponse.getStatusCode()));
    assertNull(expectedResponse.getBody());

    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link UserController#activate(Authentication, String, Boolean)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test
  public void testActivateNull() {
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    final ResponseEntity<Void> actual =
        getController().activate(getMockAuthentication(), ID.toString(), null);

    verifyNoMoreInteractions(getMockAuthentication());
    verifyNoMoreInteractions(getMockService());

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link UserController#activate(Authentication, String, Boolean)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test
  public void testActivateNotFound() {
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);

    final String[] adminAuthorities = getController().getAdminAuthorities();
    assertNotNull(adminAuthorities);
    final Collection<GrantedAuthority> userAuthorities = new ArrayList<>(adminAuthorities.length);
    for (final String adminAuth : getController().getAdminAuthorities()) {
      userAuthorities.add(new SimpleGrantedAuthority(adminAuth));
    }

    when(getMockAuthentication().getAuthorities()).then(invocation -> userAuthorities);

    when(getMockService().setEnabled(ID, false)).thenReturn(null);

    final ResponseEntity<Void> actual =
        getController().activate(getMockAuthentication(), ID.toString(), Boolean.FALSE);

    verify(getMockAuthentication(), times(1)).getAuthorities();
    verifyNoMoreInteractions(getMockAuthentication());
    verify(getMockService(), times(1)).setEnabled(ID, false);
    verifyNoMoreInteractions(getMockService());

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link UserController#activate(Authentication, String, Boolean)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test
  public void testActivateNotFoundException() {
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);

    final String[] adminAuthorities = getController().getAdminAuthorities();
    assertNotNull(adminAuthorities);
    final Collection<GrantedAuthority> userAuthorities = new ArrayList<>(adminAuthorities.length);
    for (final String adminAuth : getController().getAdminAuthorities()) {
      userAuthorities.add(new SimpleGrantedAuthority(adminAuth));
    }

    when(getMockAuthentication().getAuthorities()).then(invocation -> userAuthorities);
    when(getMockService().setEnabled(ID, false)).thenThrow(new UserNotFoundException());

    final ResponseEntity<Void> actual =
        getController().activate(getMockAuthentication(), ID.toString(), Boolean.FALSE);

    verify(getMockAuthentication(), times(1)).getAuthorities();
    verifyNoMoreInteractions(getMockAuthentication());
    verify(getMockService(), times(1)).setEnabled(ID, false);
    verifyNoMoreInteractions(getMockService());

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link UserController#activate(Authentication, String, Boolean)}.
   */
  @Test
  public void testActivateIdIllegal() {
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);

    final String[] adminAuthorities = getController().getAdminAuthorities();
    assertNotNull(adminAuthorities);
    final Collection<GrantedAuthority> userAuthorities = new ArrayList<>(adminAuthorities.length);
    for (final String adminAuth : getController().getAdminAuthorities()) {
      userAuthorities.add(new SimpleGrantedAuthority(adminAuth));
    }

    when(getMockAuthentication().getAuthorities()).then(invocation -> userAuthorities);

    final ResponseEntity<Void> actual =
        getController().activate(getMockAuthentication(), "this_is_not_a_UUID", Boolean.FALSE);

    verify(getMockAuthentication(), times(1)).getAuthorities();
    verifyNoMoreInteractions(getMockAuthentication());
    verifyNoMoreInteractions(getMockService());

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link UserController#register(RegisterRequest)}.
   * 
   * @throws EntityNotFoundException if a default entity associated to a new user account is not
   *         found.
   */
  @Test
  public void testRegister() {
    final RegistrationDto model = new RegistrationDto();
    model.setUsername(USERNAME);
    model.setEmail(EMAIL);
    model.setPassword(PASSWORD);
    model.setMatchingPassword(PASSWORD);
    final User user =
        User.builder(USERNAME, EMAIL).password(PASSWORD.clone()).id(UUID.randomUUID()).build();

    final ResponseEntity<User> expectedResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);

    when(getMockService().register(model)).thenReturn(true);
    when(getMockService().findByEmail(model.getEmail())).thenReturn(user);

    final ResponseEntity<Void> actualResponse = getController().register(model, request);

    assertThat(actualResponse, is(expectedResponse));

    verify(getMockService(), times(1)).register(model);
    verify(getMockService(), times(1)).findByEmail(model.getEmail());
    verify(getMockService(), times(1)).update(user);
    verifyNoMoreInteractions(getMockService());
  }

  /**
   * Test method for {@link UserController#register(RegisterRequest)}.
   * 
   * @throws EntityNotFoundException if a default entity associated to a new user account is not
   *         found.
   */
  @Test
  public void testRegisterAlreadyExists() {
    final RegistrationDto model = new RegistrationDto();
    model.setUsername(USERNAME);
    model.setEmail(EMAIL);
    model.setPassword(PASSWORD);
    model.setMatchingPassword(PASSWORD);

    final ResponseEntity<User> expectedResponse = new ResponseEntity<>(HttpStatus.CONFLICT);

    when(getMockService().register(model)).thenReturn(false);

    final ResponseEntity<Void> actualResponse = getController().register(model, request);

    assertThat(actualResponse, is(expectedResponse));

    verify(getMockService(), times(1)).register(model);
    verifyNoMoreInteractions(getMockService());
  }

  /**
   * Test method for {@link UserController#register(RegisterRequest)}.
   * 
   * @throws EntityNotFoundException if a default entity associated to a new user account is not
   *         found.
   */
  @Test
  public void testRegisterNotFoundException() {
    final RegistrationDto model = new RegistrationDto();
    model.setUsername(USERNAME);
    model.setEmail(EMAIL);
    model.setPassword(PASSWORD);
    model.setMatchingPassword(PASSWORD);

    final ResponseEntity<User> expectedResponse = new ResponseEntity<>(HttpStatus.CONFLICT);

    when(getMockService().register(model)).thenThrow(new RoleNotFoundException());

    final ResponseEntity<Void> actualResponse = getController().register(model, request);

    assertThat(actualResponse, is(expectedResponse));

    verify(getMockService(), times(1)).register(model);
    verifyNoMoreInteractions(getMockService());
  }

  /**
   * Test method for {@link UserController#sendVerification(String, WebRequest)}.
   */
  @Test
  public void testSendVerification() {
    final User model = User.builder(USERNAME, EMAIL).id(ID).build();
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);

    when(getMockService().findByEmail(EMAIL)).thenReturn(model);

    final ResponseEntity<Void> actual = getController().sendVerification(EMAIL, request);

    verify(getMockService(), times(1)).findByEmail(EMAIL);
    verifyNoMoreInteractions(getMockService());

    assertThat(actual.getStatusCode(), is(expectedResponse.getStatusCode()));
    assertNull(expectedResponse.getBody());

    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link UserController#sendVerification(String, WebRequest)}.
   */
  @Test
  public void testSendVerificationNotFound() {
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);

    when(getMockService().findByEmail(EMAIL)).thenReturn(null);

    final ResponseEntity<Void> actual = getController().sendVerification(EMAIL, request);

    verify(getMockService(), times(1)).findByEmail(EMAIL);
    verifyNoMoreInteractions(getMockService());

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link UserController#sendVerification(String, WebRequest)}.
   */
  @Test
  public void testSendVerificationNotFoundException() {
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);

    when(getMockService().findByEmail(EMAIL)).thenThrow(new UserNotFoundException());

    final ResponseEntity<Void> actual = getController().sendVerification(EMAIL, request);

    verify(getMockService(), times(1)).findByEmail(EMAIL);
    verifyNoMoreInteractions(getMockService());

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link UserController#sendVerification(String, WebRequest)}.
   */
  @Test
  public void testSendVerificationAlreadyVerified() {
    final User model = User.builder(USERNAME, EMAIL).verified(true).id(ID).build();
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

    when(getMockService().findByEmail(EMAIL)).thenReturn(model);

    final ResponseEntity<Void> actual = getController().sendVerification(EMAIL, request);

    verify(getMockService(), times(1)).findByEmail(EMAIL);
    verifyNoMoreInteractions(getMockService());

    assertThat(actual.getStatusCode(), is(expectedResponse.getStatusCode()));
    assertNull(expectedResponse.getBody());

    assertNull(actual.getBody());
  }



  /**
   * Test method for {@link UserController#verify(Authentication, String, String)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   * @throws VerificationTokenNotFoundException if the verification token entity is not found.
   */
  @Test
  public void testVerify() {
    final VerificationToken verificationToken = new VerificationToken();
    final User model = User.builder(USERNAME, EMAIL).id(ID).build();
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);

    final String[] adminAuthorities = getController().getAdminAuthorities();
    assertNotNull(adminAuthorities);
    final Collection<GrantedAuthority> userAuthorities = new ArrayList<>(adminAuthorities.length);
    for (final String adminAuth : getController().getAdminAuthorities()) {
      userAuthorities.add(new SimpleGrantedAuthority(adminAuth));
    }

    when(getMockAuthentication().getAuthorities()).then(invocation -> userAuthorities);
    when(getMockAuthentication().getDetails()).thenReturn(null);
    when(verificationService.findByUserAndCode(ID, TOKEN)).thenReturn(verificationToken);
    when(getMockService().verify(ID)).thenReturn(model);

    final ResponseEntity<Void> actual =
        getController().verify(getMockAuthentication(), ID.toString(), TOKEN);

    verify(getMockAuthentication(), times(1)).getAuthorities();
    verify(getMockAuthentication(), times(1)).getDetails();
    verifyNoMoreInteractions(getMockAuthentication());

    verify(verificationService, times(1)).findByUserAndCode(ID, TOKEN);
    verify(verificationService, times(1)).update(verificationToken);
    verifyNoMoreInteractions(verificationService);

    verify(getMockService(), times(1)).verify(ID);
    verify(getMockService(), times(1)).update(model);
    verifyNoMoreInteractions(getMockService());

    assertThat(actual.getStatusCode(), is(expectedResponse.getStatusCode()));
    assertNull(expectedResponse.getBody());

    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link UserController#verify(Authentication, String, String)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   * @throws VerificationTokenNotFoundException if the verification token entity is not found.
   */
  @Test
  public void testVerifyNotAdmin() {
    final VerificationToken verificationToken = new VerificationToken();
    final User model = User.builder(USERNAME, EMAIL).id(ID).build();
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);
    final UUID principalId = null;

    when(getMockAuthentication().getAuthorities()).thenReturn(Collections.emptyList());
    when(getMockAuthentication().getDetails()).thenReturn(null);
    when(verificationService.findByUserAndCode(ID, TOKEN)).thenReturn(verificationToken);
    when(getMockService().verifyByOwner(ID, principalId)).thenReturn(model);

    final ResponseEntity<Void> actual =
        getController().verify(getMockAuthentication(), ID.toString(), TOKEN);

    verify(getMockAuthentication(), times(1)).getAuthorities();
    verify(getMockAuthentication(), times(2)).getDetails();
    verifyNoMoreInteractions(getMockAuthentication());

    verify(verificationService, times(1)).findByUserAndCode(ID, TOKEN);
    verify(verificationService, times(1)).update(verificationToken);
    verifyNoMoreInteractions(verificationService);

    verify(getMockService(), times(1)).verifyByOwner(ID, principalId);
    verify(getMockService(), times(1)).update(model);
    verifyNoMoreInteractions(getMockService());

    assertThat(actual.getStatusCode(), is(expectedResponse.getStatusCode()));
    assertNull(expectedResponse.getBody());

    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link UserController#verify(Authentication, String, String)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   * @throws VerificationTokenNotFoundException if the verification token entity is not found.
   */
  @Test
  public void testVerifyNotFound() {
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);

    when(verificationService.findByUserAndCode(ID, TOKEN)).thenReturn(null);

    final ResponseEntity<Void> actual =
        getController().verify(getMockAuthentication(), ID.toString(), TOKEN);

    verifyNoMoreInteractions(getMockAuthentication());

    verify(verificationService, times(1)).findByUserAndCode(ID, TOKEN);
    verifyNoMoreInteractions(verificationService);

    verifyNoMoreInteractions(getMockService());

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link UserController#verify(Authentication, String, String)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   * @throws VerificationTokenNotFoundException if the verification token entity is not found.
   */
  @Test
  public void testVerifyUserNotFound() {
    final VerificationToken verificationToken = new VerificationToken();
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);

    final String[] adminAuthorities = getController().getAdminAuthorities();
    assertNotNull(adminAuthorities);
    final Collection<GrantedAuthority> userAuthorities = new ArrayList<>(adminAuthorities.length);
    for (final String adminAuth : getController().getAdminAuthorities()) {
      userAuthorities.add(new SimpleGrantedAuthority(adminAuth));
    }

    when(getMockAuthentication().getAuthorities()).then(invocation -> userAuthorities);

    when(verificationService.findByUserAndCode(ID, TOKEN)).thenReturn(verificationToken);
    when(getMockService().verify(ID)).thenReturn(null);

    final ResponseEntity<Void> actual =
        getController().verify(getMockAuthentication(), ID.toString(), TOKEN);

    verify(getMockAuthentication(), times(1)).getAuthorities();
    verifyNoMoreInteractions(getMockAuthentication());

    verify(verificationService, times(1)).findByUserAndCode(ID, TOKEN);
    verifyNoMoreInteractions(verificationService);

    verify(getMockService(), times(1)).verify(ID);
    verifyNoMoreInteractions(getMockService());

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link UserController#verify(Authentication, String, String)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   * @throws VerificationTokenNotFoundException if the verification token entity is not found.
   */
  @Test
  public void testVerifyVerificationTokenNotFoundException() {
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);

    when(verificationService.findByUserAndCode(ID, TOKEN))
        .thenThrow(new VerificationTokenNotFoundException());

    final ResponseEntity<Void> actual =
        getController().verify(getMockAuthentication(), ID.toString(), TOKEN);

    verifyNoMoreInteractions(getMockAuthentication());

    verify(verificationService, times(1)).findByUserAndCode(ID, TOKEN);
    verifyNoMoreInteractions(verificationService);

    verifyNoMoreInteractions(getMockService());

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link UserController#verify(Authentication, String, String)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   * @throws VerificationTokenNotFoundException if the verification token entity is not found.
   */
  @Test
  public void testVerifyUserNotFoundException() {
    final VerificationToken verificationToken = new VerificationToken();
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);

    final String[] adminAuthorities = getController().getAdminAuthorities();
    assertNotNull(adminAuthorities);
    final Collection<GrantedAuthority> userAuthorities = new ArrayList<>(adminAuthorities.length);
    for (final String adminAuth : getController().getAdminAuthorities()) {
      userAuthorities.add(new SimpleGrantedAuthority(adminAuth));
    }

    when(getMockAuthentication().getAuthorities()).then(invocation -> userAuthorities);

    when(verificationService.findByUserAndCode(ID, TOKEN)).thenReturn(verificationToken);
    when(getMockService().verify(ID)).thenThrow(new UserNotFoundException());

    final ResponseEntity<Void> actual =
        getController().verify(getMockAuthentication(), ID.toString(), TOKEN);

    verify(getMockAuthentication(), times(1)).getAuthorities();
    verifyNoMoreInteractions(getMockAuthentication());

    verify(verificationService, times(1)).findByUserAndCode(ID, TOKEN);
    verifyNoMoreInteractions(verificationService);

    verify(getMockService(), times(1)).verify(ID);
    verifyNoMoreInteractions(getMockService());

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }

  /**
   * Test method for {@link UserController#verify(Authentication, String, String)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test
  public void testVerifyIdIllegal() {
    final ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);

    final ResponseEntity<Void> actual =
        getController().verify(getMockAuthentication(), "this_is_not_a_UUID", TOKEN);

    verifyNoMoreInteractions(getMockAuthentication());
    verifyNoMoreInteractions(getMockService());

    assertThat(actual, is(expectedResponse));
    assertNull(actual.getBody());
  }

}
