package com.monogramm.starter.api.user.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.madmath03.password.Passwords;
import com.monogramm.Application;
import com.monogramm.starter.SmtpServerRule;
import com.monogramm.starter.api.AbstractControllerIT;
import com.monogramm.starter.api.AbstractControllerMockIT;
import com.monogramm.starter.api.AbstractGenericController;
import com.monogramm.starter.config.data.GenericOperation;
import com.monogramm.starter.config.data.InitialDataLoader;
import com.monogramm.starter.dto.user.PasswordResetDto;
import com.monogramm.starter.dto.user.RegistrationDto;
import com.monogramm.starter.dto.user.UserDto;
import com.monogramm.starter.persistence.user.entity.PasswordResetToken;
import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.persistence.user.entity.VerificationToken;
import com.monogramm.starter.persistence.user.exception.UserNotFoundException;
import com.monogramm.starter.persistence.user.service.IPasswordResetTokenService;
import com.monogramm.starter.persistence.user.service.IVerificationTokenService;
import com.monogramm.starter.utils.validation.PasswordConfirmationDto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;


/**
 * {@link UserController} Mock Integration Test.
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
 * @see AbstractControllerIT
 * 
 * @author madmath03
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class UserControllerMockIT extends AbstractControllerMockIT {

  /**
   * The managed type of this tested controller.
   */
  public static final String TYPE = UserController.TYPE;
  /**
   * The request base path of this tested controller.
   */
  public static final String CONTROLLER_PATH = UserController.CONTROLLER_PATH;
  /**
   * The request path for registration.
   */
  public static final String REGISTER_PATH = UserController.REGISTER_PATH;
  /**
   * The request path for resetting password.
   */
  public static final String RESET_PWD_PATH = UserController.RESET_PWD_PATH;
  /**
   * The request path for verification request.
   */
  public static final String SEND_VERIFICATION_PATH = UserController.SEND_VERIFICATION_PATH;
  /**
   * The request path for user verification.
   */
  public static final String VERIFY_PATH = UserController.VERIFY_PATH;
  /**
   * The request path for changing password.
   */
  public static final String CHANGE_PWD_PATH = UserController.CHANGE_PWD_PATH;

  private static final String DUMMY_USERNAME = "Foo";
  private static final String DUMMY_EMAIL = "foo@email.com";
  private static final char[] DUMMY_PASSWORD = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};

  private UUID randomId;

  private User testCreatedBy;
  private User testOwner;
  private User testEntity;

  @Autowired
  private InitialDataLoader initialDataLoader;

  @Autowired
  private IVerificationTokenService verificationService;

  @Autowired
  private IPasswordResetTokenService passwordResetTokenService;

  @Rule
  public SmtpServerRule smtpServerRule = new SmtpServerRule(2525);

  @Before
  public void setUp() {
    super.setUpMockMvc();
    super.setUpValidUser(GenericOperation.allPermissionNames(TYPE));

    this.randomId = UUID.randomUUID();

    // Add the users
    testCreatedBy =
        User.builder(DUMMY_USERNAME + "_Creator", DUMMY_USERNAME + ".creator@creation.org").build();
    assertTrue(getUserService().add(testCreatedBy));
    testOwner =
        User.builder(DUMMY_USERNAME + "_Owner", DUMMY_USERNAME + ".owner@creation.org").build();
    assertTrue(getUserService().add(testOwner));

    // Add a test user
    testEntity =
        User.builder(DUMMY_USERNAME, DUMMY_EMAIL).createdBy(testCreatedBy).owner(testOwner).build();
    assertTrue(getUserService().add(testEntity));
  }

  @After
  public void tearDown() {
    super.deleteUser(testEntity);
    testEntity = null;

    super.deleteUser(testCreatedBy);
    testCreatedBy = null;

    super.deleteUser(testOwner);
    testOwner = null;
  }

  /**
   * Test method for
   * {@link UserController#getDataById(String, org.springframework.web.context.request.WebRequest, javax.servlet.http.HttpServletResponse)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetUserByIdRandomId() throws Exception {
    // No permission returned
    MvcResult result = getMockMvc()
        .perform(get(CONTROLLER_PATH + '/' + randomId).headers(getHeaders(getMockToken())))
        .andExpect(status().isNotFound()).andReturn();

    assertNotNull(result.getResponse());
    assertNotNull(result.getResponse().getContentAsString());
  }

  /**
   * Test method for
   * {@link UserController#getDataById(String, org.springframework.web.context.request.WebRequest, javax.servlet.http.HttpServletResponse)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetUserById() throws Exception {
    // User previously created should be returned
    getMockMvc()
        .perform(get(CONTROLLER_PATH + '/' + this.testEntity.getId())
            .headers(getHeaders(getMockToken())))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.id", equalToIgnoringCase(this.testEntity.getId().toString())))
        .andExpect(jsonPath("$.username", equalToIgnoringCase(DUMMY_USERNAME)))
        .andExpect(jsonPath("$.email", equalToIgnoringCase(DUMMY_EMAIL)))
        .andExpect(jsonPath("$.enabled", equalTo(Boolean.TRUE)))
        .andExpect(jsonPath("$.verified", equalTo(Boolean.FALSE)))
        .andExpect(jsonPath("$.createdAt", notNullValue()))
        .andExpect(jsonPath("$.createdBy", notNullValue()))
        .andExpect(jsonPath("$.createdBy", equalToIgnoringCase(testCreatedBy.getId().toString())))
        .andExpect(jsonPath("$.modifiedAt", nullValue()))
        .andExpect(jsonPath("$.modifiedBy", nullValue()))
        .andExpect(jsonPath("$.owner", notNullValue()))
        .andExpect(jsonPath("$.owner", equalToIgnoringCase(testOwner.getId().toString())));
  }

  /**
   * Test method for {@link UserController#getAllData()}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetAllUsers() throws Exception {
    // There should at least be the test entities...
    int expectedSize = 4;
    // ...plus the users created at application initialization
    if (initialDataLoader.getUsers() != null) {
      expectedSize += initialDataLoader.getUsers().size();
    }

    // We assume the environment is freshly created with only the initial data and test data
    getMockMvc().perform(get(CONTROLLER_PATH).headers(getHeaders(getMockToken())))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$", hasSize(expectedSize)));
  }

  /**
   * Test method for
   * {@link UserController#getAllDataPaginated(int, int, org.springframework.web.context.request.WebRequest, org.springframework.web.util.UriComponentsBuilder, javax.servlet.http.HttpServletResponse)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetAllUsersPaginated() throws Exception {
    int expectedSize = 1;

    // There should at least be the test entities...
    int maxSize = 4;
    // ...plus the users created at application initialization
    if (initialDataLoader.getUsers() != null) {
      maxSize += initialDataLoader.getUsers().size();
    }

    getMockMvc()
        .perform(get(CONTROLLER_PATH).param("page", "0").param("size", "1")
            .headers(getHeaders(getMockToken())))
        .andExpect(status().isOk())
        .andExpect(header().string("Link",
            "<http://localhost/spring-rest-api-starter-it/api/users?page=1&size=1>; rel=\"next\", "
                + "<http://localhost/spring-rest-api-starter-it/api/users?page=" + maxSize
                + "&size=1>; rel=\"last\""))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$", hasSize(expectedSize)));
  }

  /**
   * Test method for
   * {@link UserController#getAllDataPaginated(int, int, org.springframework.web.context.request.WebRequest, org.springframework.web.util.UriComponentsBuilder, javax.servlet.http.HttpServletResponse)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetAllUsersPaginatedDefaultSize() throws Exception {
    // There should at least be the test entity...
    int expectedSize = 4;
    // ...plus the users created at application initialization
    if (initialDataLoader.getUsers() != null) {
      expectedSize += initialDataLoader.getUsers().size();
    }
    expectedSize = Math.min(expectedSize, AbstractGenericController.DEFAULT_SIZE_INT);

    getMockMvc()
        .perform(get(CONTROLLER_PATH).param("page", "0").headers(getHeaders(getMockToken())))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$", hasSize(expectedSize)));
  }

  /**
   * Test method for
   * {@link UserController#addData(com.monogramm.starter.persistence.user.entity.User, org.springframework.web.util.UriComponentsBuilder)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testAddUser() throws Exception {
    final String newUsername = "Bar";
    final String newEmail = "bar@email.com";
    final User model = User.builder(newUsername, newEmail).build();
    final UserDto dto = getUserService().toDto(model);

    final String userJson = dto.toJson();

    // Insert test user should work
    getMockMvc()
        .perform(post(CONTROLLER_PATH).headers(getHeaders(getMockToken())).content(userJson))
        .andExpect(status().isCreated()).andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.username", equalToIgnoringCase(newUsername)))
        .andExpect(jsonPath("$.email", equalToIgnoringCase(newEmail)))
        .andExpect(jsonPath("$.createdAt", notNullValue()))
        .andExpect(jsonPath("$.createdBy", nullValue()))
        .andExpect(jsonPath("$.modifiedAt", nullValue()))
        .andExpect(jsonPath("$.modifiedBy", nullValue()));

    // Insert again should generate conflict
    getMockMvc()
        .perform(post(CONTROLLER_PATH).headers(getHeaders(getMockToken())).content(userJson))
        .andExpect(status().isConflict()).andExpect(content().bytes(new byte[] {}));
  }

  /**
   * Test method for {@link UserController#updateData(String, User)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test
  public void testUpdateUser() throws Exception {
    // Update on random UUID should not find any user
    final User dummyModel = User.builder("God", "god@creation.org").id(randomId).build();
    final UserDto dummyDto = getUserService().toDto(dummyModel);
    getMockMvc().perform(put(CONTROLLER_PATH + '/' + randomId).headers(getHeaders(getMockToken()))
        .content(dummyDto.toJson())).andExpect(status().isNotFound());

    // Update the user
    final String newEmail = "new@email.com";
    this.testEntity.setEmail(newEmail);
    this.testEntity.setModifiedBy(testOwner);
    final UserDto dto = getUserService().toDto(this.testEntity);

    // Update test user should work
    final String entityJson = this.testEntity.toJson();
    final String dtoJson = dto.toJson();

    assertEquals(dtoJson, entityJson);

    getMockMvc()
        .perform(put(CONTROLLER_PATH + '/' + this.testEntity.getId())
            .headers(getHeaders(getMockToken())).content(dtoJson))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.id", equalToIgnoringCase(this.testEntity.getId().toString())))
        .andExpect(jsonPath("$.username", equalToIgnoringCase(DUMMY_USERNAME)))
        .andExpect(jsonPath("$.email", equalToIgnoringCase(newEmail)))
        .andExpect(jsonPath("$.enabled", equalTo(Boolean.TRUE)))
        .andExpect(jsonPath("$.verified", equalTo(Boolean.FALSE)))
        .andExpect(jsonPath("$.createdAt", notNullValue()))
        .andExpect(jsonPath("$.createdBy", notNullValue()))
        .andExpect(jsonPath("$.createdBy", equalToIgnoringCase(testCreatedBy.getId().toString())))
        .andExpect(jsonPath("$.modifiedAt", notNullValue()))
        .andExpect(jsonPath("$.modifiedBy", notNullValue()))
        .andExpect(jsonPath("$.modifiedBy", equalToIgnoringCase(testOwner.getId().toString())))
        .andExpect(jsonPath("$.owner", notNullValue()))
        .andExpect(jsonPath("$.owner", equalToIgnoringCase(testOwner.getId().toString())));
  }

  /**
   * Test method for {@link UserController#deleteData(java.lang.String)}.
   * 
   * @throws UserNotFoundException if the user entity to delete is not found.
   */
  @Test
  public void testDeleteUser() throws Exception {
    // Delete on random UUID should not find any user
    getMockMvc()
        .perform(delete(CONTROLLER_PATH + '/' + randomId).headers(getHeaders(getMockToken())))
        .andExpect(status().isNotFound());

    // Delete test user should work
    getMockMvc().perform(
        delete(CONTROLLER_PATH + '/' + this.testEntity.getId()).headers(getHeaders(getMockToken())))
        .andExpect(status().isNoContent());
  }

  /**
   * Test method for {@link UserController#getUserByUsernameOrEmail(String, String)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testGetUserByUsernameOrEmail() throws Exception {
    // No user returned
    getMockMvc()
        .perform(get(CONTROLLER_PATH + "/get").param("username", "").param("email", "")
            .headers(getHeaders(getMockToken())))
        .andExpect(status().isNotFound()).andExpect(content().bytes(new byte[] {}));

    // User previously created should be returned
    getMockMvc()
        .perform(get(CONTROLLER_PATH + "/get").param("username", DUMMY_USERNAME)
            .param("email", DUMMY_EMAIL).headers(getHeaders(getMockToken())))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.id", equalToIgnoringCase(this.testEntity.getId().toString())))
        .andExpect(jsonPath("$.username", equalToIgnoringCase(DUMMY_USERNAME)))
        .andExpect(jsonPath("$.email", equalToIgnoringCase(DUMMY_EMAIL)))
        .andExpect(jsonPath("$.enabled", equalTo(Boolean.TRUE)))
        .andExpect(jsonPath("$.verified", equalTo(Boolean.FALSE)))
        .andExpect(jsonPath("$.createdAt", notNullValue()))
        .andExpect(jsonPath("$.createdBy", notNullValue()))
        .andExpect(jsonPath("$.createdBy", equalToIgnoringCase(testCreatedBy.getId().toString())))
        .andExpect(jsonPath("$.modifiedAt", nullValue()))
        .andExpect(jsonPath("$.modifiedBy", nullValue()))
        .andExpect(jsonPath("$.owner", notNullValue()))
        .andExpect(jsonPath("$.owner", equalToIgnoringCase(testOwner.getId().toString())));
  }

  /**
   * Test method for
   * {@link UserController#resetPassword(String, org.springframework.web.context.request.WebRequest)}
   * and {@link UserController#resetPassword(com.monogramm.starter.dto.user.PasswordResetDto)}.
   *
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test
  public void testResetPassword() throws Exception {
    // Resetting password on random email should not find any user
    getMockMvc().perform(post(RESET_PWD_PATH).content("dummy_email"))
        .andExpect(status().isNoContent());

    // Request to send a password reset email should work
    getMockMvc().perform(post(RESET_PWD_PATH).content(this.testEntity.getEmail()))
        .andExpect(status().isNoContent());

    // Retrieve password reset request and check its content
    final List<PasswordResetToken> tokens = passwordResetTokenService.findAll();
    assertNotNull(tokens);
    assertEquals(1, tokens.size());
    assertNotNull(tokens.get(0));
    assertNotNull(tokens.get(0).getCode());
    final PasswordResetToken token = tokens.get(0);
    final String tokenCode = token.getCode();
    final Date initialExpiration = token.getExpiryDate();
    assertNotNull(initialExpiration);

    // Check email received and its content
    MimeMessage[] receivedMessages = smtpServerRule.getMessages();
    assertEquals(1, receivedMessages.length);
    final Object mailContent = receivedMessages[0].getContent();
    assertTrue(((String) mailContent).contains(tokenCode));

    final char[] password = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
    final PasswordResetDto dto =
        new PasswordResetDto(this.testEntity.getEmail(), token.getCode(), password, password);

    getMockMvc().perform(put(RESET_PWD_PATH).headers(getHeaders()).content(toJsonBytes(dto)))
        .andExpect(status().isNoContent());

    final PasswordResetToken updatedToken = passwordResetTokenService.findById(token.getId());
    assertTrue(initialExpiration.after(updatedToken.getExpiryDate()));

    passwordResetTokenService.deleteById(token.getId());
  }

  /**
   * Test method for {@link UserController#changePassword(String, PasswordConfirmationDto)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test
  public void testChangePassword() throws Exception {
    // Update on random UUID should not find any user
    final char[] password = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
    final PasswordConfirmationDto dto = new PasswordConfirmationDto(password, password);

    getMockMvc().perform(put(CHANGE_PWD_PATH + "/" + randomId).headers(getHeaders(getMockToken()))
        .content(toJsonBytes(dto))).andExpect(status().isNotFound());

    // Update test user password should work
    final byte[] passwordJsonBytes = toJsonBytes(dto);
    getMockMvc()
        .perform(put(CHANGE_PWD_PATH + "/" + this.testEntity.getId())
            .headers(getHeaders(getMockToken())).content(passwordJsonBytes))
        .andExpect(status().isNoContent());

    final User updatedModel = getUserService().findByEmail(DUMMY_EMAIL);
    assertNotNull(updatedModel.getPassword());
    assertTrue(Passwords.isExpectedPassword(password, updatedModel.getPassword()));
  }

  /**
   * Test method for {@link UserController#activate(String, Boolean)}.
   * 
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test
  public void testActivate() throws Exception {
    // Activating on random UUID should not find any user
    getMockMvc()
        .perform(put(CONTROLLER_PATH + "/" + randomId + "/activate")
            .headers(getHeaders(getMockToken())).content(toJsonBytes(Boolean.TRUE)))
        .andExpect(status().isNotFound());

    // Update test user activation should work
    getMockMvc()
        .perform(put(CONTROLLER_PATH + "/" + this.testEntity.getId() + "/activate")
            .headers(getHeaders(getMockToken())).content(toJsonBytes(Boolean.FALSE)))
        .andExpect(status().isNoContent());

    final User updatedModel = getUserService().findByEmail(DUMMY_EMAIL);
    assertEquals(Boolean.FALSE, updatedModel.isEnabled());
  }

  /**
   * Test method for
   * {@link UserController#register(RegistrationDto, org.springframework.web.context.request.WebRequest)}
   * and {@link UserController#verify(String)}.
   * 
   * @throws Exception if the test crashes.
   */
  @Test
  public void testRegisterAndVerify() throws Exception {
    final String username = "Bar";
    final String email = "bar@monogramm.io";
    final RegistrationDto model = new RegistrationDto();
    model.setUsername(username);
    model.setEmail(email);
    model.setPassword(DUMMY_PASSWORD);
    model.setMatchingPassword(DUMMY_PASSWORD);

    final String userJson = toJson(model);

    // Register test user should work
    getMockMvc()
        .perform(post(REGISTER_PATH).contentType(MediaType.APPLICATION_JSON_UTF8).content(userJson))
        .andExpect(status().isNoContent()).andExpect(content().bytes(new byte[] {}));

    // Check verification token generated
    final List<VerificationToken> tokens = verificationService.findAll();
    assertNotNull(tokens);
    assertEquals(1, tokens.size());
    assertNotNull(tokens.get(0));
    assertNotNull(tokens.get(0).getCode());
    final VerificationToken token = tokens.get(0);
    final Date initialExpiration = token.getExpiryDate();
    assertNotNull(initialExpiration);

    // Check email received and its content
    MimeMessage[] receivedMessages = smtpServerRule.getMessages();
    assertEquals(1, receivedMessages.length);
    final Object mailContent = receivedMessages[0].getContent();
    assertTrue(((String) mailContent).contains(token.getCode()));

    // Register again should generate conflict
    getMockMvc()
        .perform(post(REGISTER_PATH).contentType(MediaType.APPLICATION_JSON_UTF8).content(userJson))
        .andExpect(status().isConflict()).andExpect(content().bytes(new byte[] {}));

    // Verifying on random UUID should not find any user
    getMockMvc().perform(put(VERIFY_PATH + "/" + randomId).headers(getHeaders(getMockToken()))
        .content(token.getCode())).andExpect(status().isNotFound());

    // Update test user verification should work
    final User registeredUser = getUserService().findByEmail(model.getEmail());
    assertNotNull(registeredUser);
    assertNotNull(registeredUser.getId());

    getMockMvc().perform(put(VERIFY_PATH + "/" + registeredUser.getId())
        .headers(getHeaders(getMockToken())).content(token.getCode()))
        .andExpect(status().isNoContent());

    final VerificationToken updatedToken = verificationService.findById(token.getId());
    assertTrue(initialExpiration.after(updatedToken.getExpiryDate()));

    verificationService.deleteById(token.getId());
  }

  /**
   * Test method for
   * {@link UserController#sendVerification(String, org.springframework.web.context.request.WebRequest)}
   * and {@link UserController#verify(String)}.
   *
   * @throws UserNotFoundException if the user entity to update is not found.
   */
  @Test
  public void testSendVerificationAndVerify() throws Exception {
    // Verifying already verified user should not send any email but work anyway
    getMockMvc().perform(post(SEND_VERIFICATION_PATH).headers(getHeaders(getMockToken()))
        .content(getTestUser().getEmail())).andExpect(status().isOk());

    List<VerificationToken> tokens = verificationService.findAll();
    assertNotNull(tokens);
    assertEquals(0, tokens.size());

    // Request to send a verification email should work
    getMockMvc().perform(post(SEND_VERIFICATION_PATH).headers(getHeaders(getMockToken()))
        .content(this.testEntity.getEmail())).andExpect(status().isNoContent());

    // Check verification token generated
    tokens = verificationService.findAll();
    assertNotNull(tokens);
    assertEquals(1, tokens.size());
    assertNotNull(tokens.get(0));
    assertNotNull(tokens.get(0).getCode());
    final VerificationToken token = tokens.get(0);
    final Date initialExpiration = token.getExpiryDate();
    assertNotNull(initialExpiration);

    // Check email received and its content
    MimeMessage[] receivedMessages = smtpServerRule.getMessages();
    assertEquals(1, receivedMessages.length);
    final Object mailContent = receivedMessages[0].getContent();
    assertTrue(((String) mailContent).contains(token.getCode()));

    // Verifying on random UUID should not find any user
    getMockMvc().perform(put(VERIFY_PATH + "/" + randomId).headers(getHeaders(getMockToken()))
        .content(token.getCode())).andExpect(status().isNotFound());

    // Update test user verification should work
    getMockMvc()
        .perform(put(VERIFY_PATH + "/" + this.testEntity.getId())
            .headers(getHeaders(getMockToken())).content(token.getCode()))
        .andExpect(status().isNoContent());

    final User updatedModel = getUserService().findByEmail(DUMMY_EMAIL);
    assertEquals(Boolean.TRUE, updatedModel.isVerified());

    final VerificationToken updatedToken = verificationService.findById(token.getId());
    assertTrue(initialExpiration.after(updatedToken.getExpiryDate()));

    verificationService.deleteById(token.getId());
  }

}
