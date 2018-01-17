package com.monogramm.starter.persistence.user.service;

import com.monogramm.starter.dto.user.RegistrationDto;
import com.monogramm.starter.dto.user.UserDto;
import com.monogramm.starter.persistence.EntityNotFoundException;
import com.monogramm.starter.persistence.GenericService;
import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.persistence.user.exception.UserNotFoundException;

import java.util.List;
import java.util.UUID;

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
   * Activate a user account.
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
