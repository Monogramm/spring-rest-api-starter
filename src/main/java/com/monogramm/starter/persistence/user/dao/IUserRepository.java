package com.monogramm.starter.persistence.user.dao;

import com.monogramm.starter.persistence.GenericRepository;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.List;
import java.util.UUID;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * The {@link User} Data Access Object (DAO) interface.
 * 
 * @author madmath03
 */
@Repository
public interface IUserRepository extends GenericRepository<User> {

  /**
   * Find all users from the repository containing the username while ignoring case.
   * 
   * @param username the username content to search.
   * 
   * @return the list of all the users matching the search through the repository.
   */
  @Transactional(readOnly = true)
  @Query("FROM User AS u WHERE LOWER(u.username) LIKE concat('%', LOWER(:username), '%')")
  List<User> findAllContainingUsernameIgnoreCase(@Param("username") final String username);

  /**
   * Find all users from the repository containing the email while ignoring case.
   * 
   * @param email the email content to search.
   * 
   * @return the list of all the users matching the search through the repository.
   */
  @Transactional(readOnly = true)
  @Query("FROM User AS u WHERE LOWER(u.email) LIKE concat('%', LOWER(:email), '%')")
  List<User> findAllContainingEmailIgnoreCase(@Param("email") final String email);

  /**
   * Find all users from the repository containing the username or email while ignoring case.
   * 
   * @param username the username content to search.
   * @param email the email content to search.
   * 
   * @return the list of all the users matching the search through the repository.
   */
  @Transactional(readOnly = true)
  @Query("FROM User AS u WHERE LOWER(u.username) LIKE concat('%', LOWER(:username), '%') "
      + "OR LOWER(u.email) LIKE concat('%', LOWER(:email), '%')")
  List<User> findAllContainingUsernameOrEmailIgnoreCase(@Param("username") final String username,
      @Param("email") final String email);

  /**
   * Find an user account through its username while ignoring case.
   * 
   * @param username the username to search.
   * 
   * @return the user account matching the username.
   * 
   * @throws NoResultException if no user matches the username in the repository.
   * @throws NonUniqueResultException if several users match the username in the repository.
   */
  @Transactional(readOnly = true)
  User findByUsernameIgnoreCase(final String username);

  /**
   * Find an user account through its email while ignoring case.
   * 
   * @param email the email to search.
   * 
   * @return the user account matching the email.
   * 
   * @throws NoResultException if no user matches the email in the repository.
   * @throws NonUniqueResultException if several users match the email in the repository.
   */
  @Transactional(readOnly = true)
  User findByEmailIgnoreCase(final String email);

  /**
   * Find an user account through its username or email while ignoring case.
   * 
   * @param username the username to search.
   * @param email the email to search.
   * 
   * @return the user account matching the username or email.
   * 
   * @throws NoResultException if no user matches the username or email in the repository.
   * @throws NonUniqueResultException if several users match the username or email in the
   *         repository.
   */
  @Transactional(readOnly = true)
  User findByUsernameOrEmailIgnoreCase(final String username, final String email);

  /**
   * Set the password of a user account.
   * 
   * @param userId the user account identifier.
   * @param password the clear password to hash and set.
   * 
   * @return the updated user account, {@code null} if user account was not found in persistence
   *         layer.
   */
  default User setPassword(final UUID userId, final char[] password) {
    User updateEntity = this.findById(userId);

    if (updateEntity != null) {
      updateEntity.setPassword(password);

      updateEntity = this.save(updateEntity);
    }

    return updateEntity;
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
   * @param owner the entity owner.
   * 
   * @return the updated user account, {@code null} if user account was not found in persistence
   *         layer.
   */
  default User setPasswordByOwner(final UUID userId, final char[] password, final User owner) {
    User updateEntity = this.findByIdAndOwner(userId, owner);

    if (updateEntity != null) {
      updateEntity.setPassword(password);

      updateEntity = this.save(updateEntity);
    }

    return updateEntity;
  }

  /**
   * Set the active status of a user account.
   * 
   * @param userId the user account identifier.
   * @param enabled the enabled status to set.
   * 
   * @return the updated user account, {@code null} if user account was not found in persistence
   *         layer.
   */
  default User setEnabled(final UUID userId, final boolean enabled) {
    User updateEntity = this.findById(userId);

    if (updateEntity != null) {
      updateEntity.setEnabled(enabled);

      updateEntity = this.save(updateEntity);
    }

    return updateEntity;
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
   * @return the updated user account, {@code null} if user account was not found in persistence
   *         layer.
   */
  default User setEnabledByOwner(final UUID userId, final boolean enabled, final User owner) {
    User updateEntity = this.findByIdAndOwner(userId, owner);

    if (updateEntity != null) {
      updateEntity.setEnabled(enabled);

      updateEntity = this.save(updateEntity);
    }

    return updateEntity;
  }

  /**
   * Set the verified status of a user account.
   * 
   * @param userId the user account identifier.
   * @param verified the verified status to set.
   * 
   * @return the updated user account, {@code null} if user account was not found in persistence
   *         layer.
   */
  default User setVerified(final UUID userId, final boolean verified) {
    User updateEntity = this.findById(userId);

    if (updateEntity != null) {
      updateEntity.setVerified(verified);

      updateEntity = this.save(updateEntity);
    }

    return updateEntity;
  }

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
   * @param owner the entity owner.
   * 
   * @return the updated user account, {@code null} if user account was not found in persistence
   *         layer.
   */
  default User setVerifiedByOwner(final UUID userId, final boolean verified, final User owner) {
    User updateEntity = this.findByIdAndOwner(userId, owner);

    if (updateEntity != null) {
      updateEntity.setVerified(verified);

      updateEntity = this.save(updateEntity);
    }

    return updateEntity;
  }

  /**
   * Tests if an entity exists in the repository for the given primary key or the username or the
   * email.
   * 
   * @param userId the identifier of the user account to check existence.
   * @param username the username of the user account to check existence.
   * @param email the email of the user account to check existence.
   * 
   * @return {@code true} if user account exists, {@code false} otherwise.
   */
  @Transactional(readOnly = true)
  @Query("SELECT count(u) > 0 FROM User AS u WHERE u.id = :userId "
      + "OR LOWER(u.username) = LOWER(:username) OR LOWER(u.email) = LOWER(:email)")
  boolean exists(@Param("userId") final UUID userId, @Param("username") final String username,
      @Param("email") final String email);
}
