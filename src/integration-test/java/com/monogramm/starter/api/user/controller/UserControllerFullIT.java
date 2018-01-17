package com.monogramm.starter.api.user.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.github.madmath03.password.Passwords;
import com.monogramm.Application;
import com.monogramm.starter.api.AbstractControllerFullIT;
import com.monogramm.starter.config.data.GenericOperation;
import com.monogramm.starter.config.data.InitialDataLoader;
import com.monogramm.starter.dto.user.PasswordResetDto;
import com.monogramm.starter.dto.user.RegistrationDto;
import com.monogramm.starter.dto.user.UserDto;
import com.monogramm.starter.persistence.user.entity.PasswordResetToken;
import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.persistence.user.entity.VerificationToken;
import com.monogramm.starter.persistence.user.exception.PasswordResetTokenNotFoundException;
import com.monogramm.starter.persistence.user.exception.UserNotFoundException;
import com.monogramm.starter.persistence.user.exception.VerificationTokenNotFoundException;
import com.monogramm.starter.persistence.user.service.IPasswordResetTokenService;
import com.monogramm.starter.persistence.user.service.IVerificationTokenService;
import com.monogramm.starter.utils.validation.PasswordConfirmationDto;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * {@link UserController} Integration Test.
 * 
 * <p>
 * We assume the environment is freshly created and only contains the initial data.
 * </p>
 * 
 * <p>
 * Spring boot test is searching {@code @SpringBootConfiguration} or {@code @SpringBootApplication}.
 * In this case it will automatically find {@link Application} boot main class.
 * </p>
 * 
 * @see Application
 * @see InitialDataLoader
 * @see AbstractControllerFullIT
 * 
 * @author madmath03
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserControllerFullIT extends AbstractControllerFullIT {

  /**
   * The managed type of this tested controller.
   */
  public static final String TYPE = "Users";
  /**
   * The request base path of this tested controller.
   */
  public static final String CONTROLLER_PATH = '/' + TYPE;

  private static final String DUMMY_USERNAME = "Foo";
  private static final String DUMMY_EMAIL = "foo@email.com";
  private static final char[] DUMMY_PASSWORD = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
  private static final String DUMMY_TOKEN = "Foo";

  private User testCreatedBy;
  private User testOwner;
  private User testEntity;
  private UserDto testDto;

  private String accessToken;

  @Autowired
  private IVerificationTokenService verificationService;

  @Autowired
  private IPasswordResetTokenService passwordResetTokenService;

  @Before
  public void setUp() throws URISyntaxException {
    // Set up a valid user for authentication and such
    super.setUpValidUser(GenericOperation.allPermissionNames(TYPE));

    // Get an access token for later calls to API
    this.accessToken = this.getFullToken();

    testCreatedBy = this.createUser(DUMMY_USERNAME + "_Creator",
        DUMMY_USERNAME + ".creator@creation.org", null, null);

    testOwner = this.createUser(DUMMY_USERNAME + "_Owner", DUMMY_USERNAME + ".owner@creation.org",
        null, null);

    // Add a user
    testEntity =
        User.builder(DUMMY_USERNAME, DUMMY_EMAIL).createdBy(testCreatedBy).owner(testOwner).build();
    assertTrue(getUserService().add(testEntity));
    testDto = getUserService().toDto(testEntity);
  }

  @After
  public void tearDown() throws URISyntaxException {
    // Tear valid user for authentication
    super.tearDownValidUser();

    // Revoke an access token
    this.revokeToken(this.accessToken);

    this.deleteUser(testEntity);
    testDto = null;

    this.deleteUser(testCreatedBy);
    testCreatedBy = null;

    this.deleteUser(testOwner);
    testOwner = null;
  }

  /**
   * Test method for {@link UserController#getDataById(java.lang.String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetUserById() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH, "/", testEntity.getId());
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<UserDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, UserDto.class);

    final UserDto dto = responseEntity.getBody();

    assertNotNull(dto);
    assertEquals(this.testDto, dto);
    assertNotNull(dto.getId());
    assertNotNull(dto.getCreatedAt());
    assertNotNull(dto.getCreatedBy());
    assertEquals(testCreatedBy.getId(), dto.getCreatedBy());
    assertNull(dto.getModifiedAt());
    assertNull(dto.getModifiedBy());
    assertNotNull(dto.getOwner());
    assertEquals(testOwner.getId(), dto.getOwner());
  }

  /**
   * Test method for {@link UserController#getDataById(java.lang.String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetUserByIdNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH, "/", testEntity.getId());
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<UserDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, UserDto.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link UserController#getAllData()}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetAllUsers() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH);
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<UserDto[]> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, UserDto[].class);

    final UserDto[] dtos = responseEntity.getBody();

    assertNotNull(dtos);
    assertTrue(Arrays.stream(dtos).anyMatch(a -> DUMMY_USERNAME.equals(a.getUsername())));
    assertTrue(Arrays.stream(dtos).anyMatch(a -> testCreatedBy.getId().equals(a.getCreatedBy())));
    assertTrue(Arrays.stream(dtos).anyMatch(a -> testOwner.getId().equals(a.getOwner())));
  }

  /**
   * Test method for {@link UserController#getAllData()}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetAllUsersNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH);
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<Object> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, Object.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  /**
   * Test method for
   * {@link UserController#addData(com.monogramm.starter.persistence.user.entity.User, org.springframework.web.util.UriComponentsBuilder)}.
   * 
   * @throws UserNotFoundException if the user added is not found at the end of the test.
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testAddUser() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH);

    final User model =
        User.builder("God", "god@creation.org").enabled(false).verified(false).build();
    final UserDto dto = getUserService().toDto(model);

    final HttpEntity<UserDto> requestEntity = new HttpEntity<>(dto, headers);

    final ResponseEntity<UserDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, UserDto.class);

    final UserDto content = responseEntity.getBody();
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    assertNotNull(content);
    assertNotNull(content.getId());

    // FIXME This test should be rollbacked
    getUserService().deleteById(content.getId());

    assertNotNull(content.getCreatedAt());
    assertNull(content.getCreatedBy());
    assertNull(content.getModifiedAt());
    assertNull(content.getModifiedBy());
    assertNull(content.getOwner());
    assertEquals(dto.getUsername(), content.getUsername());
    assertEquals(dto.getEmail(), content.getEmail());

    assertNotNull(responseEntity.getHeaders().getLocation());
  }

  /**
   * Test method for
   * {@link UserController#addData(com.monogramm.starter.persistence.user.entity.User, org.springframework.web.util.UriComponentsBuilder)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testAddUserNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH);

    final User model =
        User.builder("God", "god@creation.org").enabled(false).verified(false).build();
    final UserDto dto = getUserService().toDto(model);

    final HttpEntity<UserDto> requestEntity = new HttpEntity<>(dto, headers);

    final ResponseEntity<UserDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, UserDto.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link UserController#updateData(String, User)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testUpdateUser() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH, "/", this.testEntity.getId());

    this.testEntity.setUsername("Bar");
    this.testDto = getUserService().toDto(this.testEntity);

    final HttpEntity<UserDto> requestEntity = new HttpEntity<>(this.testDto, headers);

    final ResponseEntity<UserDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.PUT, requestEntity, UserDto.class);

    final UserDto content = responseEntity.getBody();
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(content);
    assertEquals(this.testDto, content);
    assertEquals(this.testDto.getUsername(), content.getUsername());
  }

  /**
   * Test method for {@link UserController#updateData(String, User)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testUpdateUserNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH, "/", this.testEntity.getId());

    this.testEntity.setUsername("Bar");
    this.testDto = getUserService().toDto(this.testEntity);

    final HttpEntity<UserDto> requestEntity = new HttpEntity<>(this.testDto, headers);

    final ResponseEntity<UserDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.PUT, requestEntity, UserDto.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link UserController#deleteData(java.lang.String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testDeleteUser() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH, "/", this.testEntity.getId());

    final HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<Void> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.DELETE, requestEntity, Void.class);

    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link UserController#deleteData(java.lang.String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testDeleteUserNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH, "/", this.testEntity.getId());

    final HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<Void> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.DELETE, requestEntity, Void.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }



  /**
   * Test method for {@link UserController#getUserByUsernameOrEmail(String, String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetUserByUsernameOrEmail() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(new Object[] {CONTROLLER_PATH, "/get"},
        "username=" + testEntity.getUsername() + "&email=" + testEntity.getEmail());
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<UserDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, UserDto.class);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    final UserDto dto = responseEntity.getBody();

    assertNotNull(dto);
    assertEquals(this.testDto, dto);
    assertNotNull(dto.getId());
    assertNotNull(dto.getCreatedAt());
    assertNotNull(dto.getCreatedBy());
    assertEquals(testCreatedBy.getId(), dto.getCreatedBy());
    assertNull(dto.getModifiedAt());
    assertNull(dto.getModifiedBy());
    assertNotNull(dto.getOwner());
    assertEquals(testOwner.getId(), dto.getOwner());
  }

  /**
   * Test method for {@link UserController#getUserByUsernameOrEmail(String, String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testGetUserByUsernameOrEmailNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH,
        "/get?username=" + testEntity.getUsername() + "&email=" + testEntity.getEmail());
    final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    final ResponseEntity<UserDto> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, UserDto.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }



  /**
   * Test method for
   * {@link UserController#resetPassword(String, org.springframework.web.context.request.WebRequest)}.
   * 
   * @throws URISyntaxException if the test crashes.
   */
  @Test
  public void testResetPassword() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH, "/reset_password");

    final HttpEntity<String> requestEntity = new HttpEntity<>(this.testEntity.getEmail(), headers);

    final ResponseEntity<Void> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, Void.class);

    // Password reset is only possible for anonymous user
    assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
  }

  /**
   * Test method for
   * {@link UserController#resetPassword(String, org.springframework.web.context.request.WebRequest)}
   * and {@link UserController#resetPassword(com.monogramm.starter.dto.user.PasswordResetDto)}.
   * 
   * @throws URISyntaxException if the test crashes.
   * @throws PasswordResetTokenNotFoundException if the password reset token is not found.
   */
  @Test
  public void testResetPasswordNoAuthorization()
      throws URISyntaxException, PasswordResetTokenNotFoundException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH, "/reset_password");

    final HttpEntity<String> requestEntity = new HttpEntity<>(this.testEntity.getEmail(), headers);

    final ResponseEntity<Void> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, Void.class);

    final Void responseBody = responseEntity.getBody();
    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    assertNull(responseBody);

    this.pause(5, TimeUnit.SECONDS,
        () -> assertEquals(1, passwordResetTokenService.findAll().size()));

    final List<PasswordResetToken> tokens = passwordResetTokenService.findAll();
    assertNotNull(tokens);
    assertEquals(1, tokens.size());
    assertNotNull(tokens.get(0));
    assertNotNull(tokens.get(0).getCode());
    final String tokenCode = tokens.get(0).getCode();

    final char[] password = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
    final PasswordResetDto dto =
        new PasswordResetDto(this.testEntity.getEmail(), tokenCode, password, password);
    final HttpEntity<PasswordResetDto> resetRequestEntity = new HttpEntity<>(dto, headers);

    final ResponseEntity<Void> resetResponseEntity =
        getRestTemplate().exchange(url, HttpMethod.PUT, resetRequestEntity, Void.class);

    assertEquals(HttpStatus.NO_CONTENT, resetResponseEntity.getStatusCode());

    // FIXME This test should be rollbacked
    for (final PasswordResetToken token : tokens) {
      if (this.testEntity.getEmail().equals(token.getUser().getEmail())) {
        passwordResetTokenService.deleteById(token.getId());
      }
    }
  }

  /**
   * Test method for {@link UserController#changePassword(String, PasswordConfirmationDto)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testChangePassword() throws URISyntaxException {
    final char[] password = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
    final PasswordConfirmationDto dto = new PasswordConfirmationDto(password, password);

    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH, "/change_password/", this.testEntity.getId());

    final HttpEntity<PasswordConfirmationDto> requestEntity = new HttpEntity<>(dto, headers);

    final ResponseEntity<Void> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.PUT, requestEntity, Void.class);

    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

    final User updatedModel = getUserService().findByEmail(DUMMY_EMAIL);
    assertNotNull(updatedModel.getPassword());
    assertTrue(Passwords.isExpectedPassword(password, updatedModel.getPassword()));
  }

  /**
   * Test method for {@link UserController#changePassword(String, PasswordConfirmationDto)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testChangePasswordNoAuthorization() throws URISyntaxException {
    final char[] password = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
    final PasswordConfirmationDto dto = new PasswordConfirmationDto(password, password);

    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH, "/change_password/", this.testEntity.getId());

    final HttpEntity<PasswordConfirmationDto> requestEntity = new HttpEntity<>(dto, headers);

    final ResponseEntity<Void> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.PUT, requestEntity, Void.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link UserController#activate(String, Boolean)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testActivate() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH, "/", this.testEntity.getId(), "/activate");

    final HttpEntity<Boolean> requestEntity = new HttpEntity<>(Boolean.FALSE, headers);

    final ResponseEntity<Void> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.PUT, requestEntity, Void.class);

    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

    final User updatedModel = getUserService().findByEmail(DUMMY_EMAIL);
    assertEquals(Boolean.FALSE, updatedModel.isEnabled());
  }

  /**
   * Test method for {@link UserController#activate(String, Boolean)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testActivateNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH, "/", this.testEntity.getId(), "/activate");

    final HttpEntity<Boolean> requestEntity = new HttpEntity<>(Boolean.FALSE, headers);

    final ResponseEntity<Void> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.PUT, requestEntity, Void.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link UserController#register(RegistrationDto)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testRegister() throws URISyntaxException {
    final String username = "Bar";
    final String email = "bar@monogramm.io";
    final RegistrationDto model = new RegistrationDto();
    model.setUsername(username);
    model.setEmail(email);
    model.setPassword(DUMMY_PASSWORD);
    model.setMatchingPassword(DUMMY_PASSWORD);

    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH, "/register");

    final HttpEntity<RegistrationDto> requestEntity = new HttpEntity<>(model, headers);

    final ResponseEntity<Void> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, Void.class);

    // Registration is only possible for anonymous user
    assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link UserController#register(RegistrationDto)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   * @throws VerificationTokenNotFoundException if a verification token cannot be found.
   * @throws UserNotFoundException if a user cannot be found.
   */
  @Test
  public void testRegisterNoAuthorization()
      throws URISyntaxException, VerificationTokenNotFoundException, UserNotFoundException {
    final String username = "Bar";
    final String email = "bar@monogramm.io";
    final RegistrationDto model = new RegistrationDto();
    model.setUsername(username);
    model.setEmail(email);
    model.setPassword(DUMMY_PASSWORD);
    model.setMatchingPassword(DUMMY_PASSWORD);

    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH, "/register");

    final HttpEntity<RegistrationDto> requestEntity = new HttpEntity<>(model, headers);

    final ResponseEntity<Void> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, Void.class);

    final Void responseBody = responseEntity.getBody();
    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    assertNull(responseBody);

    final User registeredUser = getUserService().findByUsernameOrEmail(username, email);
    assertNotNull(registeredUser);

    // FIXME This test should be rollbacked
    final List<VerificationToken> tokens = verificationService.findAll();
    for (final VerificationToken token : tokens) {
      if (email.equals(token.getUser().getEmail())) {
        verificationService.deleteById(token.getId());
      }
    }
    getUserService().deleteById(registeredUser.getId());
  }

  /**
   * Test method for {@link UserController#sendVerification(String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   * @throws VerificationTokenNotFoundException if a verification token cannot be found.
   */
  @Test
  public void testSendVerification() throws URISyntaxException, VerificationTokenNotFoundException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH, "/send_verification");

    final HttpEntity<String> requestEntity = new HttpEntity<>(this.testEntity.getEmail(), headers);

    final ResponseEntity<Void> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, Void.class);

    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

    this.pause(5, TimeUnit.SECONDS, () -> assertFalse(verificationService.findAll().isEmpty()));

    final List<VerificationToken> tokens = verificationService.findAll();
    assertNotNull(tokens);
    assertEquals(1, tokens.size());
    assertNotNull(tokens.get(0));
    assertNotNull(tokens.get(0).getCode());

    // FIXME This test should be rollbacked
    for (final VerificationToken token : tokens) {
      if (this.testEntity.getEmail().equals(token.getUser().getEmail())) {
        verificationService.deleteById(token.getId());
      }
    }
  }

  /**
   * Test method for {@link UserController#verify(String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   * @throws VerificationTokenNotFoundException
   */
  @Test
  public void testVerify() throws URISyntaxException, VerificationTokenNotFoundException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final VerificationToken token = new VerificationToken();
    token.setUser(this.testEntity);
    verificationService.add(token);

    final String url = this.getUrl(CONTROLLER_PATH, "/verify/", this.testEntity.getId());

    final HttpEntity<String> requestEntity = new HttpEntity<>(token.getCode(), headers);

    final ResponseEntity<Void> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.PUT, requestEntity, Void.class);

    // FIXME This test should be rollbacked
    verificationService.deleteById(token.getId());

    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

    final User updatedModel = getUserService().findByEmail(this.testEntity.getEmail());
    assertEquals(Boolean.TRUE, updatedModel.isVerified());
  }

  /**
   * Test method for {@link UserController#verify(String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testVerifyNotFound() throws URISyntaxException {
    final HttpHeaders headers = getHeaders(this.accessToken);

    final String url = this.getUrl(CONTROLLER_PATH, "/verify/", this.testEntity.getId());

    final HttpEntity<String> requestEntity = new HttpEntity<>(DUMMY_TOKEN, headers);

    final ResponseEntity<Void> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.PUT, requestEntity, Void.class);

    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
  }

  /**
   * Test method for {@link UserController#verify(String)}.
   * 
   * @throws URISyntaxException if the URL could not be created.
   */
  @Test
  public void testVerifyNoAuthorization() throws URISyntaxException {
    final HttpHeaders headers = getHeaders();

    final String url = this.getUrl(CONTROLLER_PATH, "/verify/", this.testEntity.getId());

    final HttpEntity<String> requestEntity = new HttpEntity<>(DUMMY_TOKEN, headers);

    final ResponseEntity<Void> responseEntity =
        getRestTemplate().exchange(url, HttpMethod.PUT, requestEntity, Void.class);

    assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
  }

}
