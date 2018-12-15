package com.monogramm.starter.api.user.controller;

import com.monogramm.starter.api.AbstractGenericController;
import com.monogramm.starter.api.user.event.OnPasswordResetEvent;
import com.monogramm.starter.api.user.event.OnRegistrationCompleteEvent;
import com.monogramm.starter.config.OAuth2WebSecurityConfig;
import com.monogramm.starter.config.data.GenericOperation;
import com.monogramm.starter.dto.user.PasswordResetDto;
import com.monogramm.starter.dto.user.RegistrationDto;
import com.monogramm.starter.dto.user.UserDto;
import com.monogramm.starter.persistence.EntityNotFoundException;
import com.monogramm.starter.persistence.user.entity.PasswordResetToken;
import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.persistence.user.entity.VerificationToken;
import com.monogramm.starter.persistence.user.exception.PasswordResetTokenNotFoundException;
import com.monogramm.starter.persistence.user.exception.UserNotFoundException;
import com.monogramm.starter.persistence.user.exception.VerificationTokenNotFoundException;
import com.monogramm.starter.persistence.user.service.IPasswordResetTokenService;
import com.monogramm.starter.persistence.user.service.IUserService;
import com.monogramm.starter.persistence.user.service.IVerificationTokenService;
import com.monogramm.starter.utils.validation.PasswordConfirmationDto;
import com.monogramm.starter.utils.validation.ValidUuid;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * The User API Controller.
 * 
 * @author madmath03
 */
@RestController
public class UserController extends AbstractGenericController<User, UserDto> {

  /**
   * Logger for {@link UserController}.
   */
  private static final Logger LOG = LogManager.getLogger(UserController.class);

  /**
   * The main data type handled by this controller.
   */
  public static final String TYPE = "Users";
  /**
   * The request base path of this controller.
   */
  public static final String CONTROLLER_PATH = '/' + "users";
  /**
   * The request path for registration.
   */
  public static final String REGISTER_PATH = CONTROLLER_PATH + "/register";
  /**
   * The request path for resetting password.
   */
  public static final String RESET_PWD_PATH = CONTROLLER_PATH + "/reset_password";
  /**
   * The request path for verification request.
   */
  public static final String SEND_VERIFICATION_PATH = CONTROLLER_PATH + "/send_verification";
  /**
   * The request path for user verification.
   */
  public static final String VERIFY_PATH = CONTROLLER_PATH + "/verify";
  /**
   * The request path for changing password.
   */
  public static final String CHANGE_PWD_PATH = CONTROLLER_PATH + "/change_password";

  /**
   * The Authority data type of this controller.
   */
  public static final String AUTH_TYPE = "USERS";

  /**
   * The Authority for the reading operations of this controller.
   * 
   * @see GenericOperation#READ
   */
  public static final String AUTH_READ = OAuth2WebSecurityConfig.AUTH_PREFIX + AUTH_TYPE
      + GenericOperation.PERM_SEP + GenericOperation.PERM_READ;
  /**
   * The Authority for the listing operations of this controller.
   * 
   * @see GenericOperation#LIST
   */
  public static final String AUTH_LIST = OAuth2WebSecurityConfig.AUTH_PREFIX + AUTH_TYPE
      + GenericOperation.PERM_SEP + GenericOperation.PERM_LIST;
  /**
   * The Authority for the creation operations of this controller.
   * 
   * @see GenericOperation#LIST
   */
  public static final String AUTH_CREATE = OAuth2WebSecurityConfig.AUTH_PREFIX + AUTH_TYPE
      + GenericOperation.PERM_SEP + GenericOperation.PERM_CREATE;
  /**
   * The Authority for the update operations of this controller.
   * 
   * @see GenericOperation#LIST
   */
  public static final String AUTH_UPDATE = OAuth2WebSecurityConfig.AUTH_PREFIX + AUTH_TYPE
      + GenericOperation.PERM_SEP + GenericOperation.PERM_UPDATE;
  /**
   * The Authority for the listing operations of this controller.
   * 
   * @see GenericOperation#LIST
   */
  public static final String AUTH_DELETE = OAuth2WebSecurityConfig.AUTH_PREFIX + AUTH_TYPE
      + GenericOperation.PERM_SEP + GenericOperation.PERM_DELETE;

  private ApplicationEventPublisher eventPublisher;

  private IVerificationTokenService verificationTokenService;

  private IPasswordResetTokenService passwordResetTokenService;

  /**
   * Create a {@link UserController}.
   * 
   * @param userService the users service.
   * @param eventPublisher the event publisher.
   * @param verificationTokenService the verification token service.
   * @param passwordResetTokenService the password reset token service.
   */
  @Autowired
  public UserController(IUserService userService, ApplicationEventPublisher eventPublisher,
      IVerificationTokenService verificationTokenService,
      IPasswordResetTokenService passwordResetTokenService) {
    super(userService);

    this.eventPublisher = eventPublisher;
    this.verificationTokenService = verificationTokenService;
    this.passwordResetTokenService = passwordResetTokenService;
  }

  @Override
  protected String getControllerPath() {
    return CONTROLLER_PATH;
  }

  @Override
  protected IUserService getService() {
    return (IUserService) super.getService();
  }

  @Override
  @RequestMapping(value = CONTROLLER_PATH + "/{id}", method = RequestMethod.GET)
  @PreAuthorize(value = "hasAuthority('" + AUTH_READ + "')")
  public ResponseEntity<UserDto> getDataById(@PathVariable @ValidUuid String id) {
    return super.getDataById(id);
  }

  @Override
  @RequestMapping(value = CONTROLLER_PATH, method = RequestMethod.GET)
  @PreAuthorize(value = "hasAuthority('" + AUTH_LIST + "')")
  public ResponseEntity<List<UserDto>> getAllData() {
    return super.getAllData();
  }

  @Override
  @RequestMapping(value = CONTROLLER_PATH, method = RequestMethod.POST,
      consumes = "application/json")
  @PreAuthorize(value = "hasAuthority('" + AUTH_CREATE + "')")
  public ResponseEntity<UserDto> addData(@RequestBody UserDto dto, UriComponentsBuilder builder) {
    return super.addData(dto, builder);
  }

  @Override
  @RequestMapping(value = CONTROLLER_PATH + "/{id}", method = RequestMethod.PUT,
      consumes = "application/json")
  @PreAuthorize(value = "hasAuthority('" + AUTH_UPDATE + "')")
  public ResponseEntity<UserDto> updateData(@PathVariable @ValidUuid String id,
      @RequestBody UserDto dto) {
    return super.updateData(id, dto);
  }

  @Override
  @RequestMapping(value = CONTROLLER_PATH + "/{id}", method = RequestMethod.DELETE)
  @PreAuthorize(value = "hasAuthority('" + AUTH_DELETE + "')")
  public ResponseEntity<Void> deleteData(@PathVariable @ValidUuid String id) {
    return super.deleteData(id);
  }



  /**
   * Find a {@link User} entity by its unique username or email.
   * 
   * <p>
   * Returns a {@link UserDto} JSON representation about a single user.
   * </p>
   * 
   * @param username <em>Optional URL Parameter:</em> the user name, {@code null} by default.
   * @param email <em>Optional URL Parameter:</em> the user email address, {@code null} by default.
   * 
   * @return
   *         <ul>
   * 
   *         <li>
   *         <p>
   *         <strong>Success Response:</strong>
   *         </p>
   * 
   *         <ul>
   *         <li>
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.OK</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> a {@link UserDto} JSON representation of the entity
   *         {@link User}
   *         </p>
   *         </li>
   *         </ul>
   * 
   *         </li>
   * 
   *         <li>
   *         <p>
   *         <strong>Error Response:</strong>
   *         </p>
   * 
   *         <ul>
   *         <li>
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.NOT_FOUND</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> <code>null</code>
   *         </p>
   *         </li>
   *         </ul>
   * 
   *         </li>
   * 
   *         </ul>
   */
  @RequestMapping(value = CONTROLLER_PATH + "/get", method = RequestMethod.GET)
  @PreAuthorize(value = "hasAuthority('" + AUTH_READ + "')")
  public ResponseEntity<UserDto> getUserByUsernameOrEmail(
      @RequestParam(required = false) String username,
      @RequestParam(required = false) String email) {
    UserDto dto = null;
    HttpStatus status;

    final User entity = this.getService().findByUsernameOrEmail(username, email);
    if (entity == null) {
      status = HttpStatus.NOT_FOUND;
    } else {
      dto = this.getService().toDto(entity);
      status = HttpStatus.OK;
    }

    return new ResponseEntity<>(dto, status);
  }



  /**
   * Send an email to reset password.
   * 
   * <p>
   * Send an email to the user's email address containing a password reset code.
   * </p>
   * 
   * @param email <em>Required Body Content:</em> the user account email address.
   * @param request the Web Request.
   * 
   * @return
   *         <ul>
   * 
   *         <li>
   * 
   *         <p>
   *         <strong>Success and Error Response:</strong>
   *         </p>
   * 
   *         <ul>
   *         <li>
   * 
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.NO_CONTENT</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> <code>{}</code>
   *         </p>
   * 
   *         </li>
   *         </ul>
   * 
   *         </li>
   * 
   *         <li>
   * 
   *         </ul>
   * 
   */
  @RequestMapping(value = "/" + RESET_PWD_PATH, method = RequestMethod.POST)
  public ResponseEntity<Void> resetPassword(@RequestBody String email, WebRequest request) {
    User user;
    try {
      user = this.getService().findByEmail(email);
    } catch (UserNotFoundException e) {
      LOG.debug("resetPassword(email=" + email + ")", e);
      user = null;
    }

    HttpStatus status;
    if (user == null) {
      /*
       * Do not alert the client that no account exists. This would allow an attacker to identify
       * user accounts.
       */
      status = HttpStatus.NO_CONTENT;
    } else {
      this.sendEmailPasswordReset(user, request);
      status = HttpStatus.NO_CONTENT;
    }

    return new ResponseEntity<>(status);
  }

  /**
   * Reset a user's password.
   * 
   * @param passwordReset <em>Required Body Content:</em> a JSON data about the
   *        {@link PasswordResetDto} to reset the user password.
   * 
   * @return
   *         <ul>
   * 
   *         <li>
   * 
   *         <p>
   *         <strong>Success Response:</strong>
   *         </p>
   * 
   *         <ul>
   *         <li>
   * 
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.NO_CONTENT</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> <code>null</code>
   *         </p>
   * 
   *         </li>
   *         </ul>
   * 
   *         </li>
   * 
   *         <li>
   * 
   *         <p>
   *         <strong>Error Response when user or reset token not found:</strong>
   *         </p>
   * 
   *         <ul>
   *         <li>
   * 
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.NOT_FOUND</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> <code>null</code>
   *         </p>
   * 
   *         </li>
   *         </ul>
   * 
   *         </li>
   * 
   *         </ul>
   */
  @RequestMapping(value = "/" + RESET_PWD_PATH, method = RequestMethod.PUT)
  public ResponseEntity<Void> resetPassword(@RequestBody @Valid PasswordResetDto passwordReset) {
    HttpStatus status;

    final Date now = new Date();
    final String email = passwordReset.getEmail();
    final String token = passwordReset.getToken();
    try {
      final User user = getService().findByEmail(email);

      if (user == null) {
        status = HttpStatus.NOT_FOUND;
      } else {
        // Retrieve reset request
        final PasswordResetToken passwordResetToken =
            passwordResetTokenService.findByUserAndCode(user, token);

        // If a valid token exists and reset is successful
        if (passwordResetToken != null && now.before(passwordResetToken.getExpiryDate())
            && this.getService().setPassword(user.getId(), passwordReset.getPassword()) != null) {
          status = HttpStatus.NO_CONTENT;
          // Invalidate the token
          passwordResetToken.setExpiryDate(now);
          passwordResetTokenService.update(passwordResetToken);
        } else {
          status = HttpStatus.NOT_FOUND;
        }
      }

    } catch (PasswordResetTokenNotFoundException | UserNotFoundException e) {
      LOG.debug("resetPassword(email=" + email + ",token=" + token + ")", e);
      status = HttpStatus.NOT_FOUND;
    }

    return new ResponseEntity<>(status);
  }

  private void sendEmailPasswordReset(final User user, WebRequest request) {
    eventPublisher.publishEvent(
        new OnPasswordResetEvent(user, request.getLocale(), request.getContextPath()));
  }

  /**
   * Change a user's password.
   * 
   * <p>
   * Update a user's password.
   * </p>
   * 
   * @param id <em>Required URL Path variable:</em> universal unique identifier (i.e. {@code UUID}).
   * @param password <em>Required Body Content:</em> a JSON data about the new user password.
   * 
   * @return
   *         <ul>
   * 
   *         <li>
   * 
   *         <p>
   *         <strong>Success Response:</strong>
   *         </p>
   * 
   *         <ul>
   *         <li>
   * 
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.NO_CONTENT</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> <code>null</code>
   *         </p>
   * 
   *         </li>
   *         </ul>
   * 
   *         </li>
   * 
   *         <li>
   * 
   *         <p>
   *         <strong>Error Response:</strong>
   *         </p>
   * 
   *         <ul>
   *         <li>
   * 
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.NOT_FOUND</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> <code>null</code>
   *         </p>
   * 
   *         </li>
   *         </ul>
   * 
   *         <p>
   *         OR
   *         </p>
   * 
   *         <ul>
   *         <li>
   * 
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.BAD_REQUEST</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> <code>{}</code>
   *         </p>
   * 
   *         </li>
   *         </ul>
   * 
   *         </li>
   * 
   *         </ul>
   */
  @RequestMapping(value = CHANGE_PWD_PATH + "/{id}", method = RequestMethod.PUT,
      consumes = "application/json")
  @PreAuthorize(value = "hasAuthority('" + AUTH_UPDATE + "')")
  public ResponseEntity<Void> changePassword(@PathVariable String id,
      @RequestBody @Valid PasswordConfirmationDto password) {
    HttpStatus status;

    try {
      if (password == null) {
        status = HttpStatus.BAD_REQUEST;
      } else {
        final User user =
            this.getService().setPassword(UUID.fromString(id), password.getPassword());

        if (user == null) {
          status = HttpStatus.NOT_FOUND;
        } else {
          status = HttpStatus.NO_CONTENT;
        }
      }
    } catch (UserNotFoundException | IllegalArgumentException e) {
      LOG.debug("changePassword(id=" + id + ")", e);
      status = HttpStatus.NOT_FOUND;
    }

    return new ResponseEntity<>(status);
  }

  /**
   * Activate a user account.
   * 
   * <p>
   * Change the active status of a user account.
   * </p>
   * 
   * @param id <em>Required URL Path variable:</em> universal unique identifier (i.e. {@code UUID}).
   * @param enabled <em>Required Body Content:</em> a JSON data about the new user active status.
   * 
   * @return
   *         <ul>
   * 
   *         <li>
   * 
   *         <p>
   *         <strong>Success Response:</strong>
   *         </p>
   * 
   *         <ul>
   *         <li>
   * 
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.NO_CONTENT</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> <code>null</code>
   *         </p>
   * 
   *         </li>
   *         </ul>
   * 
   *         </li>
   * 
   *         <li>
   * 
   *         <p>
   *         <strong>Error Response:</strong>
   *         </p>
   * 
   *         <ul>
   *         <li>
   * 
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.NOT_FOUND</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> <code>null</code>
   *         </p>
   * 
   *         </li>
   *         </ul>
   * 
   *         <p>
   *         OR
   *         </p>
   * 
   *         <ul>
   *         <li>
   * 
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.BAD_REQUEST</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> <code>null</code>
   *         </p>
   * 
   *         </li>
   *         </ul>
   * 
   *         </li>
   * 
   *         </ul>
   */
  @RequestMapping(value = CONTROLLER_PATH + "/{id}/activate", method = RequestMethod.PUT,
      consumes = "application/json")
  @PreAuthorize(value = "hasAuthority('" + AUTH_UPDATE + "')")
  public ResponseEntity<Void> activate(@PathVariable String id, @RequestBody Boolean enabled) {
    HttpStatus status;

    if (enabled == null) {
      status = HttpStatus.BAD_REQUEST;
    } else {
      try {
        final User user = this.getService().setEnabled(UUID.fromString(id), enabled);

        if (user == null) {
          status = HttpStatus.NOT_FOUND;
        } else {
          status = HttpStatus.NO_CONTENT;
        }
      } catch (UserNotFoundException | IllegalArgumentException e) {
        LOG.debug("activate(id=" + id + ", enabled=" + enabled + ")", e);
        status = HttpStatus.NOT_FOUND;
      }
    }

    return new ResponseEntity<>(status);
  }

  /**
   * Register a new user.
   * 
   * <p>
   * Register a new user and send a verification email.
   * </p>
   * 
   * @param registration <em>Required Body Content:</em> a JSON data about the {@link User} to
   *        register.
   * @param request the Web Request.
   * 
   * @return
   *         <ul>
   * 
   *         <li>
   * 
   *         <p>
   *         <strong>Success Response:</strong>
   *         </p>
   * 
   *         <ul>
   *         <li>
   * 
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.NO_CONTENT</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> <code>{}</code>
   *         </p>
   * 
   *         </li>
   *         </ul>
   * 
   *         </li>
   * 
   *         <li>
   * 
   *         <p>
   *         <strong>Error Response when account could not be created:</strong>
   *         </p>
   * 
   *         <ul>
   *         <li>
   * 
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.CONFLICT</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> <code>{}</code>
   *         </p>
   *         </li>
   *         </ul>
   * 
   *         </li>
   * 
   *         </ul>
   * 
   * 
   * @throws EntityNotFoundException if a default entity associated to a new user account is not
   *         found.
   */
  @RequestMapping(value = REGISTER_PATH, method = RequestMethod.POST, consumes = "application/json")
  public ResponseEntity<Void> register(@RequestBody @Valid RegistrationDto registration,
      WebRequest request) {
    boolean registered;
    try {
      registered = this.getService().register(registration);
    } catch (EntityNotFoundException e) {
      registered = false;
    }

    HttpStatus status;
    if (registered) {
      final User user = this.getService().findByEmail(registration.getEmail());

      this.sendEmailVerificationEvent(user, request);
      status = HttpStatus.NO_CONTENT;
    } else {
      status = HttpStatus.CONFLICT;
    }

    return new ResponseEntity<>(status);
  }

  /**
   * Send a user account verification email.
   * 
   * <p>
   * Send an email to the user's email address containing a verification code.
   * </p>
   * 
   * @param email <em>Required Body Content:</em> the user account email address.
   * @param request the Web Request.
   * 
   * @return
   *         <ul>
   * 
   *         <li>
   * 
   *         <p>
   *         <strong>Success Response:</strong>
   *         </p>
   * 
   *         <ul>
   *         <li>
   * 
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.NO_CONTENT</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> <code>{}</code>
   *         </p>
   * 
   *         </li>
   *         </ul>
   * 
   *         </li>
   * 
   *         <li>
   * 
   *         <p>
   *         <strong>Success Response when user already verified:</strong>
   *         </p>
   * 
   *         <ul>
   *         <li>
   * 
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.OK</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> <code>{}</code>
   *         </p>
   * 
   *         </li>
   *         </ul>
   * 
   *         </li>
   * 
   *         <li>
   * 
   *         <p>
   *         <strong>Error Response when no user found:</strong>
   *         </p>
   * 
   *         <ul>
   *         <li>
   * 
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.NO_CONTENT</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> <code>{}</code>
   *         </p>
   * 
   *         </li>
   *         </ul>
   * 
   *         </li>
   * 
   *         </ul>
   * 
   */
  @RequestMapping(value = SEND_VERIFICATION_PATH, method = RequestMethod.POST,
      consumes = "application/json")
  @PreAuthorize(value = "hasAuthority('" + AUTH_READ + "')")
  public ResponseEntity<Void> sendVerification(@RequestBody String email, WebRequest request) {
    User user;
    try {
      user = this.getService().findByEmail(email);
    } catch (UserNotFoundException e) {
      LOG.debug("sendVerification(email=" + email + ")", e);
      user = null;
    }

    HttpStatus status;
    if (user == null) {
      /*
       * Do not alert the client that no account exists. This would allow an attacker to identify
       * user accounts.
       */
      status = HttpStatus.NO_CONTENT;
    } else if (user.isVerified()) {
      status = HttpStatus.OK;
    } else {
      this.sendEmailVerificationEvent(user, request);
      status = HttpStatus.NO_CONTENT;
    }

    return new ResponseEntity<>(status);
  }

  private void sendEmailVerificationEvent(final User user, final WebRequest request) {
    eventPublisher.publishEvent(
        new OnRegistrationCompleteEvent(user, request.getLocale(), request.getContextPath()));
  }

  /**
   * Verify a user account.
   * 
   * <p>
   * Mark a user account as verified.
   * </p>
   * 
   * @param id <em>Required URL Path variable:</em> universal unique identifier (i.e. {@code UUID}).
   * @param token <em>Required Body Content:</em> a JSON data about the
   *        {@link VerificationToken#getCode()} to verify a user account.
   * 
   * @return
   *         <ul>
   * 
   *         <li>
   * 
   *         <p>
   *         <strong>Success Response:</strong>
   *         </p>
   * 
   *         <ul>
   *         <li>
   * 
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.NO_CONTENT</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> <code>null</code>
   *         </p>
   * 
   *         </li>
   *         </ul>
   * 
   *         </li>
   * 
   *         <li>
   * 
   *         <p>
   *         <strong>Error Response:</strong>
   *         </p>
   * 
   *         <ul>
   *         <li>
   * 
   *         <p>
   *         <strong>Code:</strong> <code>HttpStatus.NOT_FOUND</code>
   *         </p>
   *         <p>
   *         <strong>Content:</strong> <code>null</code>
   *         </p>
   * 
   *         </li>
   *         </ul>
   * 
   *         </li>
   * 
   *         </ul>
   */
  @RequestMapping(value = "/" + VERIFY_PATH + "/{id}", method = RequestMethod.PUT,
      consumes = "application/json")
  @PreAuthorize(value = "hasAuthority('" + AUTH_UPDATE + "')")
  public ResponseEntity<Void> verify(@PathVariable String id, @RequestBody String token) {
    HttpStatus status;

    final Date now = new Date();
    try {
      final UUID userId = UUID.fromString(id);

      // Retrieve verification request
      final VerificationToken verificationToken =
          verificationTokenService.findByUserAndCode(userId, token);

      // If a valid token exists and verification is successful
      // TODO use more complex request body to verify email attached to token
      if (verificationToken != null && now.before(verificationToken.getExpiryDate())
          && this.getService().verify(userId) != null) {
        status = HttpStatus.NO_CONTENT;
        // Invalidate the token
        verificationToken.setExpiryDate(now);
        verificationTokenService.update(verificationToken);
      } else {
        status = HttpStatus.NOT_FOUND;
      }

    } catch (VerificationTokenNotFoundException | UserNotFoundException
        | IllegalArgumentException e) {
      LOG.debug("verify(id=" + id + ",token=" + token + ")", e);
      status = HttpStatus.NOT_FOUND;
    }

    return new ResponseEntity<>(status);
  }
}
