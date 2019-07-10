package com.monogramm.starter.api.user.controller;

import com.monogramm.starter.api.AbstractGenericController;
import com.monogramm.starter.api.user.event.OnPasswordResetEvent;
import com.monogramm.starter.api.user.event.OnRegistrationCompleteEvent;
import com.monogramm.starter.config.OAuth2WebSecurityConfig;
import com.monogramm.starter.config.data.GenericOperation;
import com.monogramm.starter.config.security.IAuthenticationFacade;
import com.monogramm.starter.dto.user.PasswordResetDto;
import com.monogramm.starter.dto.user.RegistrationDto;
import com.monogramm.starter.dto.user.UserDto;
import com.monogramm.starter.persistence.EntityNotFoundException;
import com.monogramm.starter.persistence.parameter.entity.Parameter;
import com.monogramm.starter.persistence.parameter.service.ParameterService;
import com.monogramm.starter.persistence.user.entity.PasswordResetToken;
import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.persistence.user.entity.VerificationToken;
import com.monogramm.starter.persistence.user.exception.PasswordResetTokenNotFoundException;
import com.monogramm.starter.persistence.user.exception.UserNotFoundException;
import com.monogramm.starter.persistence.user.exception.VerificationTokenNotFoundException;
import com.monogramm.starter.persistence.user.service.PasswordResetTokenService;
import com.monogramm.starter.persistence.user.service.UserService;
import com.monogramm.starter.persistence.user.service.VerificationTokenService;
import com.monogramm.starter.utils.validation.PasswordConfirmationDto;
import com.monogramm.starter.utils.validation.ValidUuid;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * The {@link User} API Controller.
 * 
 * @author madmath03
 */
@RestController
public class UserController extends AbstractGenericController<User, UserDto> {

  /**
   * Logger for {@link UserController}.
   */
  private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

  /**
   * The main data type handled by this controller.
   */
  public static final String TYPE = "Users";
  /**
   * The request base path of this controller.
   */
  public static final String CONTROLLER_PATH = SEP + "users";
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

  /**
   * The Authorities describing Administration permissions of this controller.
   */
  protected static final String[] ADMIN_AUTH = {AUTH_LIST};


  /**
   * The parameter name storing the status of the registration functionality.
   */
  public static final String REGISTRATION_ENABLED = "REGISTRATION_ENABLED";


  private ParameterService parameterService;

  private VerificationTokenService verificationTokenService;

  private PasswordResetTokenService passwordResetTokenService;

  /**
   * Create a {@link UserController}.
   * 
   * @param messageSource the i18n message source.
   * @param eventPublisher the event publisher.
   * @param userService the users service.
   * @param parameterService the parameter service.
   * @param verificationTokenService the verification token service.
   * @param passwordResetTokenService the password reset token service.
   */
  @Autowired
  public UserController(MessageSource messageSource, ApplicationEventPublisher eventPublisher,
      UserService userService, ParameterService parameterService,
      VerificationTokenService verificationTokenService,
      PasswordResetTokenService passwordResetTokenService) {
    super(messageSource, eventPublisher, userService);

    this.parameterService = parameterService;
    this.verificationTokenService = verificationTokenService;
    this.passwordResetTokenService = passwordResetTokenService;
  }

  @Override
  protected UserNotFoundException buildEntityNotFoundException(String id, WebRequest request) {
    final Locale locale = request.getLocale();
    final String msg = getMessageSource().getMessage("controller.entity_not_found",
        new String[] {User.class.getSimpleName(), id}, locale);

    return new UserNotFoundException(msg);
  }

  @Override
  protected String[] getAdminAuthorities() {
    return ADMIN_AUTH;
  }

  @Override
  protected String getControllerPath() {
    return CONTROLLER_PATH;
  }

  @Override
  protected UserService getService() {
    return (UserService) super.getService();
  }

  @Override
  @GetMapping(value = CONTROLLER_PATH + "/{id}")
  @PreAuthorize(value = "hasAuthority('" + AUTH_READ + "')")
  @PostAuthorize("hasAuthority('" + AUTH_LIST + "') || isOwner()")
  public UserDto getDataById(@PathVariable @ValidUuid String id, WebRequest request,
      HttpServletResponse response) {
    return super.getDataById(id, request, response);
  }

  @Override
  @GetMapping(value = CONTROLLER_PATH)
  @PreAuthorize(value = "hasAnyAuthority('" + AUTH_LIST + "', '" + AUTH_READ + "')")
  public List<UserDto> getAllData(
      @RequestParam(value = SORT, defaultValue = DEFAULT_SORT_QUERY) String sort,
      Authentication authentication) {
    return super.getAllData(sort, authentication);
  }

  @Override
  @GetMapping(value = CONTROLLER_PATH, params = {PAGE})
  @PreAuthorize(value = "hasAnyAuthority('" + AUTH_LIST + "', '" + AUTH_READ + "')")
  public List<UserDto> getAllDataPaginated(
      @RequestParam(value = SORT, defaultValue = DEFAULT_SORT_QUERY) String sort,
      @RequestParam(value = PAGE) int page,
      @RequestParam(value = SIZE, defaultValue = DEFAULT_SIZE) int size,
      Authentication authentication, WebRequest request, UriComponentsBuilder builder,
      HttpServletResponse response) {
    return super.getAllDataPaginated(sort, page, size, authentication, request, builder, response);
  }

  @Override
  @PostMapping(value = CONTROLLER_PATH, consumes = "application/json")
  @PreAuthorize(value = "hasAuthority('" + AUTH_CREATE + "')")
  public ResponseEntity<UserDto> addData(Authentication authentication, @RequestBody UserDto dto,
      UriComponentsBuilder builder, HttpServletResponse response) {
    return super.addData(authentication, dto, builder, response);
  }

  @Override
  @PutMapping(value = CONTROLLER_PATH + "/{id}", consumes = "application/json")
  @PreAuthorize(value = "hasAuthority('" + AUTH_UPDATE + "')")
  public ResponseEntity<UserDto> updateData(Authentication authentication,
      @PathVariable @ValidUuid String id, @RequestBody UserDto dto) {
    // TODO If user does not have ROLES_LIST authority, forbid role update
    return super.updateData(authentication, id, dto);
  }

  @Override
  @DeleteMapping(value = CONTROLLER_PATH + "/{id}")
  @PreAuthorize(value = "hasAuthority('" + AUTH_DELETE + "')")
  public ResponseEntity<Void> deleteData(Authentication authentication,
      @PathVariable @ValidUuid String id) {
    return super.deleteData(authentication, id);
  }

  @Override
  @DeleteMapping(value = CONTROLLER_PATH, params = {IDS})
  @PreAuthorize(value = "hasAuthority('" + AUTH_DELETE + "')")
  public ResponseEntity<Void> deleteAllData(Authentication authentication,
      @RequestParam(value = IDS) String ids) {
    return super.deleteAllData(authentication, ids);
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
  @GetMapping(value = CONTROLLER_PATH + "/get")
  @PreAuthorize(value = "hasAuthority('" + AUTH_READ + "')")
  @PostAuthorize("hasAuthority('" + AUTH_LIST + "') || isOwner()")
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
  @PostMapping(value = RESET_PWD_PATH)
  @PreAuthorize(value = "isAnonymous()")
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
  @PutMapping(value = RESET_PWD_PATH)
  @PreAuthorize(value = "isAnonymous() || hasAuthority('" + AUTH_UPDATE + "')")
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
    if (LOG.isDebugEnabled()) {
      LOG.debug("Requesting to send password reset email for user: {}", user);
    }
    this.getEventPublisher().publishEvent(
        new OnPasswordResetEvent(user, request.getLocale(), request.getContextPath()));
  }

  /**
   * Change a user's password.
   * 
   * <p>
   * Update a user's password.
   * </p>
   * 
   * @param authentication Authentication information. Should be automatically provided by Spring.
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
  @PutMapping(value = CHANGE_PWD_PATH + "/{id}", consumes = "application/json")
  @PreAuthorize(value = "hasAuthority('" + AUTH_UPDATE + "')")
  public ResponseEntity<Void> changePassword(Authentication authentication,
      @PathVariable @ValidUuid String id, @RequestBody @Valid PasswordConfirmationDto password) {
    HttpStatus status;

    try {
      if (password == null) {
        status = HttpStatus.BAD_REQUEST;
      } else {
        // Only activate if owner or has administration authorities
        final User user;

        final String[] adminAuthorities = this.getAdminAuthorities();
        if (adminAuthorities != null && adminAuthorities.length > 0
            && !IAuthenticationFacade.hasAnyAuthority(authentication, adminAuthorities)) {
          final UUID ownerId = IAuthenticationFacade.getPrincipalId(authentication);

          user = this.getService().setPasswordByOwner(UUID.fromString(id), password.getPassword(),
              ownerId);
        } else {
          user = this.getService().setPassword(UUID.fromString(id), password.getPassword());
        }

        if (user == null) {
          status = HttpStatus.NOT_FOUND;
        } else {
          status = HttpStatus.NO_CONTENT;

          // XXX This should be done using Spring Data Auditing
          // Set last modification user
          final User principalUser = this.getPrincipalUser(authentication);
          user.setModifiedBy(principalUser);
          this.getService().update(user);
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
   * @param authentication Authentication information. Should be automatically provided by Spring.
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
  @PutMapping(value = CONTROLLER_PATH + "/{id}/activate", consumes = "application/json")
  @PreAuthorize(value = "hasAuthority('" + AUTH_UPDATE + "')")
  public ResponseEntity<Void> activate(Authentication authentication,
      @PathVariable @ValidUuid String id, @RequestBody Boolean enabled) {
    HttpStatus status;

    if (enabled == null) {
      status = HttpStatus.BAD_REQUEST;
    } else {
      try {
        // Only activate if owner or has administration authorities
        final User user;

        final String[] adminAuthorities = this.getAdminAuthorities();
        if (adminAuthorities != null && adminAuthorities.length > 0
            && !IAuthenticationFacade.hasAnyAuthority(authentication, adminAuthorities)) {
          final UUID ownerId = IAuthenticationFacade.getPrincipalId(authentication);

          user = this.getService().setEnabledByOwner(UUID.fromString(id), enabled, ownerId);
        } else {
          user = this.getService().setEnabled(UUID.fromString(id), enabled);
        }

        if (user == null) {
          status = HttpStatus.NOT_FOUND;
        } else {
          status = HttpStatus.NO_CONTENT;

          // XXX This should be done using Spring Data Auditing
          // Set last modification user
          final User principalUser = this.getPrincipalUser(authentication);
          user.setModifiedBy(principalUser);
          this.getService().update(user);
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
   * @throws UnsupportedOperationException if registration has been disabled.
   */
  @PostMapping(value = REGISTER_PATH, consumes = "application/json")
  @PreAuthorize(value = "isAnonymous()")
  public ResponseEntity<Void> register(@RequestBody @Valid RegistrationDto registration,
      WebRequest request) {
    final Parameter regsitrationEnabled = parameterService.findByName(REGISTRATION_ENABLED);
    if (regsitrationEnabled != null && !Boolean.parseBoolean(regsitrationEnabled.getValue())) {
      throw new UnsupportedOperationException("Registration disabled!");
    }

    LOG.debug("Registering new user...");

    boolean registered;
    try {
      registered = this.getService().register(registration);
    } catch (EntityNotFoundException e) {
      registered = false;
    }

    HttpStatus status;
    if (registered) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("New user registered: {}", registration.getEmail());
      }
      final User user = this.getService().findByEmail(registration.getEmail());

      // Make the registered user owner of its own account
      user.setOwner(user);
      this.getService().update(user);

      this.sendEmailVerificationEvent(user, request);
      status = HttpStatus.NO_CONTENT;
    } else {
      LOG.debug("Conflict on user registration");
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
  @PostMapping(value = SEND_VERIFICATION_PATH, consumes = "application/json")
  @PreAuthorize(value = "hasAuthority('" + AUTH_UPDATE + "')")
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
      if (LOG.isDebugEnabled()) {
        LOG.debug("No user account found matching email: {}", email);
      }
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
    if (LOG.isDebugEnabled()) {
      LOG.debug("Requesting to send verification email for user: {}", user);
    }
    this.getEventPublisher().publishEvent(
        new OnRegistrationCompleteEvent(user, request.getLocale(), request.getContextPath()));
  }

  /**
   * Verify a user account.
   * 
   * <p>
   * Mark a user account as verified.
   * </p>
   * 
   * @param authentication Authentication information. Should be automatically provided by Spring.
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
  @PutMapping(value = VERIFY_PATH + "/{id}", consumes = "application/json")
  @PreAuthorize(value = "hasAuthority('" + AUTH_UPDATE + "')")
  public ResponseEntity<Void> verify(Authentication authentication,
      @PathVariable @ValidUuid String id, @RequestBody String token) {
    HttpStatus status;
    LOG.debug("Verifying user...");

    final Date now = new Date();
    try {
      final UUID userId = UUID.fromString(id);

      // Retrieve verification request
      final VerificationToken verificationToken =
          verificationTokenService.findByUserAndCode(userId, token);

      // TODO use more complex request body to verify email attached to token
      // If a valid token exists
      final User user;
      if (verificationToken != null && now.before(verificationToken.getExpiryDate())) {

        // Only verify if owner or has administration authorities
        final String[] adminAuthorities = this.getAdminAuthorities();
        if (adminAuthorities != null && adminAuthorities.length > 0
            && !IAuthenticationFacade.hasAnyAuthority(authentication, adminAuthorities)) {
          final UUID ownerId = IAuthenticationFacade.getPrincipalId(authentication);

          if (LOG.isDebugEnabled()) {
            LOG.debug("Verifying owned user account: {}", userId);
          }

          user = this.getService().verifyByOwner(userId, ownerId);
        } else {

          if (LOG.isDebugEnabled()) {
            LOG.debug("Verifying user account: {}", userId);
          }
          user = this.getService().verify(userId);
        }

      } else {
        if (LOG.isDebugEnabled()) {
          LOG.debug("No verification token matching: {}, {}", userId, token);
        }

        user = null;
      }

      if (user == null) {
        status = HttpStatus.NOT_FOUND;
      } else {
        status = HttpStatus.NO_CONTENT;
        // Invalidate the token
        verificationToken.setExpiryDate(now);
        verificationTokenService.update(verificationToken);

        // XXX This should be done using Spring Data Auditing
        // Set last modification user
        final User principalUser = this.getPrincipalUser(authentication);
        user.setModifiedBy(principalUser);
        this.getService().update(user);
      }

    } catch (VerificationTokenNotFoundException | UserNotFoundException
        | IllegalArgumentException e) {
      LOG.debug("verify(id=" + id + ",token=" + token + ")", e);
      status = HttpStatus.NOT_FOUND;
    }

    return new ResponseEntity<>(status);
  }

}
