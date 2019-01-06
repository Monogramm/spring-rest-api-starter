package com.monogramm.starter.persistence.user.service;

import com.monogramm.starter.dto.user.RegistrationDto;
import com.monogramm.starter.dto.user.UserDto;
import com.monogramm.starter.persistence.EntityNotFoundException;
import com.monogramm.starter.persistence.GenericService;
import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.persistence.user.exception.UserNotFoundException;

import java.util.List;
import java.util.UUID;

/**
 * {@link User} service interface.
 * 
 * @author madmath03
 */
public interface IUserService extends GenericService<User, UserDto> {

  @Override
  UserBridge getBridge();

  /**
   * Find all users containing the username.
   * 
   * @param username the username to search.
   * 
   * @return the list of all the users matching the search.
   */
  List<User> findAllContainingUsername(final String username);

  /**
   * Find all users containing the email.
   * 
   * @param email the email content to search.
   * 
   * @return the list of all the users matching the search.
   */
  List<User> findAllContainingEmail(final String email);

  /**
   * Find all users containing the username or email.
   * 
   * @param username the username content to search.
   * @param email the email content to search.
   * 
   * @return the list of all the users matching the search.
   */
  List<User> findAllContainingUsernameOrEmail(final String username, final String email);

  /**
   * Find an user account through its username.
   * 
   * @param username the username to search.
   * 
   * @return the user account matching the username, or {@code null} if none matches.
   * 
   * @throws UserNotFoundException if no user matches the username in the repository.
   */
  User findByUsername(final String username);

  /**
   * Find an user account through its email.
   * 
   * @param email the email to search.
   * 
   * @return the user account matching the email, or {@code null} if none matches.
   * 
   * @throws UserNotFoundException if no user matches the email in the repository.
   */
  User findByEmail(final String email);

  /**
   * Find an user account through its username or email.
   * 
   * @param username the username to search.
   * @param email the email to search.
   * 
   * @return the user account matching the username or email, or {@code null} if none matches.
   * 
   * @throws UserNotFoundException if no user matches the username or email in the repository.
   */
  User findByUsernameOrEmail(final String username, final String email);

  /**
   * Set the password of a user account.
   * 
   * @param userId the user account identifier.
   * @param password the clear password to hash and set.
   * 
   * @return the updated user account.
   * 
   * @throws UserNotFoundException if no user account is found.
   */
  User setPassword(final UUID userId, final char[] password);

  /**
   * Set the password of a user account.
   * 
   * <p>
   * Secure method to ensure you only update if you own the data by providing the authenticated user
   * as owner. {@link #setPassword(UUID, char[])} should be used instead if authenticated user has
   * administration permissions.
   * </p>
   * 
   * @param userId the user account identifier.
   * @param password the clear password to hash and set.
   * @param ownerId the entity identifier of the entity owner.
   * 
   * @return the updated user account.
   * 
   * @throws UserNotFoundException if no user account is found.
   */
  default User setPasswordByOwner(final UUID userId, final char[] password, final UUID ownerId) {
    final User owner = User.builder().id(ownerId).build();
    return this.setPasswordByOwner(userId, password, owner);
  }

  /**
   * Set the password of a user account.
   * 
   * <p>
   * Secure method to ensure you only update if you own the data by providing the authenticated user
   * as owner. {@link #setPassword(UUID, char[])} should be used instead if authenticated user has
   * administration permissions.
   * </p>
   * 
   * @param userId the user account identifier.
   * @param password the clear password to hash and set.
   * @param owner the entity identifier of the entity owner.
   * 
   * @return the updated user account.
   * 
   * @throws UserNotFoundException if no user account is found.
   */
  User setPasswordByOwner(final UUID userId, final char[] password, final User owner);

  /**
   * Set the active status of a user account.
   * 
   * @param userId the user account identifier.
   * @param enabled the enabled status to set.
   * 
   * @return the updated user account.
   * 
   * @throws UserNotFoundException if no user account is found.
   */
  User setEnabled(final UUID userId, final boolean enabled);

  /**
   * Set the active status of a user account.
   * 
   * <p>
   * Secure method to ensure you only update if you own the data by providing the authenticated user
   * as owner. {@link #setEnabled(UUID, boolean)} should be used instead if authenticated user has
   * administration permissions.
   * </p>
   * 
   * @param userId the user account identifier.
   * @param enabled the enabled status to set.
   * @param ownerId the entity identifier of the entity owner.
   * 
   * @return the updated user account.
   * 
   * @throws UserNotFoundException if no user account is found.
   */
  default User setEnabledByOwner(final UUID userId, final boolean enabled, final UUID ownerId) {
    final User owner = User.builder().id(ownerId).build();
    return this.setEnabledByOwner(userId, enabled, owner);
  }

  /**
   * Set the active status of a user account.
   * 
   * <p>
   * Secure method to ensure you only update if you own the data by providing the authenticated user
   * as owner. {@link #setEnabled(UUID, boolean)} should be used instead if authenticated user has
   * administration permissions.
   * </p>
   * 
   * @param userId the user account identifier.
   * @param enabled the enabled status to set.
   * @param owner the entity owner.
   * 
   * @return the updated user account.
   * 
   * @throws UserNotFoundException if no user account is found.
   */
  User setEnabledByOwner(final UUID userId, final boolean enabled, final User owner);

  /**
   * Activate a user account.
   * 
   * <p>
   * Simple alias to {@link #setEnabled(UUID, boolean)}.
   * </p>
   * 
   * @param userId the user account identifier.
   * 
   * @return the active user account.
   * 
   * @throws UserNotFoundException if no user account is found.
   */
  default User enable(final UUID userId) {
    return this.setEnabled(userId, true);
  }

  /**
   * Activate a user account.
   * 
   * <p>
   * Simple alias to {@link #setEnabledByOwner(UUID, boolean, UUID)}.
   * </p>
   * 
   * @param userId the user account identifier.
   * @param ownerId the entity identifier of the entity owner.
   * 
   * @return the active user account.
   * 
   * @throws UserNotFoundException if no user account is found.
   */
  default User enableByOwner(final UUID userId, final UUID ownerId) {
    return this.setEnabledByOwner(userId, true, ownerId);
  }

  /**
   * Set the verified status of a user account.
   * 
   * @param userId the user account identifier.
   * @param verified the verified status to set.
   * 
   * @return the updated user account.
   * 
   * @throws UserNotFoundException if no user account is found.
   */
  User setVerified(final UUID userId, final boolean verified);

  /**
   * Set the verified status of a user account.
   * 
   * <p>
   * Secure method to ensure you only update if you own the data by providing the authenticated user
   * as owner. {@link #setVerified(UUID, boolean)} should be used instead if authenticated user has
   * administration permissions.
   * </p>
   * 
   * @param userId the user account identifier.
   * @param verified the verified status to set.
   * @param ownerId the entity identifier of the entity owner.
   * 
   * @return the updated user account.
   * 
   * @throws UserNotFoundException if no user account is found.
   */
  default User setVerifiedByOwner(final UUID userId, final boolean verified, final UUID ownerId) {
    final User owner = User.builder().id(ownerId).build();
    return this.setVerifiedByOwner(userId, verified, owner);
  }

  /**
   * Set the verified status of a user account.
   * 
   * @param userId the user account identifier.
   * @param verified the verified status to set.
   * @param owner the entity owner.
   * 
   * @return the updated user account.
   * 
   * @throws UserNotFoundException if no user account is found.
   */
  User setVerifiedByOwner(final UUID userId, final boolean verified, final User owner);

  /**
   * Verify a user account.
   * 
   * @param userId the user account identifier.
   * 
   * @return the verified user account.
   * 
   * @throws UserNotFoundException if no user account is found.
   */
  default User verify(final UUID userId) {
    return this.setVerified(userId, true);
  }

  /**
   * Verify a user account.
   * 
   * @param userId the user account identifier.
   * @param ownerId the entity identifier of the entity owner.
   * 
   * @return the verified user account.
   * 
   * @throws UserNotFoundException if no user account is found.
   */
  default User verifyByOwner(final UUID userId, final UUID ownerId) {
    return this.setVerifiedByOwner(userId, true, ownerId);
  }

  /**
   * Register a new user.
   * 
   * <p>
   * Add a new s
   * </p>
   * 
   * @param registration a registration object describing the user to register.
   * 
   * @return {@code true} if the user was registered, {@code false} otherwise.
   * 
   * @throws EntityNotFoundException if a default entity associated to a new user account is not
   *         found.
   */
  boolean register(final RegistrationDto registration);
}
