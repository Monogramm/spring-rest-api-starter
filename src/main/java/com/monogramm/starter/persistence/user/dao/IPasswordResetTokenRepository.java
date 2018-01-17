/*
 * Creation by madmath03 the 2017-12-20.
 */

package com.monogramm.starter.persistence.user.dao;

import com.monogramm.starter.persistence.GenericRepository;
import com.monogramm.starter.persistence.user.entity.PasswordResetToken;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.UUID;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * The {@link PasswordResetToken}s Data Access Object (DAO) interface.
 * 
 * @author madmath03
 */
@Repository
public interface IPasswordResetTokenRepository extends GenericRepository<PasswordResetToken> {

  /**
   * Find a token through its token code and user.
   * 
   * @param user the user to search.
   * @param code the token code to search.
   * 
   * @return the token matching the token code.
   * 
   * @throws NoResultException if no token matches the code and user in the repository.
   * @throws NonUniqueResultException if several tokens match the code and user in the repository.
   */
  @Transactional(readOnly = true)
  PasswordResetToken findByUserAndCode(final User user, final String code);

  /**
   * Find a token through its token code and user id.
   * 
   * @param userId the user id to search.
   * @param code the token code to search.
   * 
   * @return the token matching the token code.
   * 
   * @throws NoResultException if no token matches the code and user id in the repository.
   * @throws NonUniqueResultException if several tokens match the code and user id in the
   *         repository.
   */
  @Transactional(readOnly = true)
  default PasswordResetToken findByUserAndCode(final UUID userId, final String code) {
    return findByUserAndCode(User.builder().id(userId).build(), code);
  }

  /**
   * Tests if an entity exists in the repository for the given primary key or the token code.
   * 
   * @param tokenId the identifier of the token to check existence.
   * @param token the token code of the reset token to check existence.
   * 
   * @return {@code true} if token exists, {@code false} otherwise.
   */
  @Transactional(readOnly = true)
  @Query("SELECT count(p) > 0 FROM PasswordResetToken AS p "
      + "WHERE p.id = :tokenId OR p.code = :code")
  boolean exists(@Param("tokenId") final UUID tokenId, @Param("code") final String token);
}
