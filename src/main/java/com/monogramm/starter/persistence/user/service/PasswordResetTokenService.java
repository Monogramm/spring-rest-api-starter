/*
 * Creation by madmath03 the 2017-12-20.
 */

package com.monogramm.starter.persistence.user.service;

import com.monogramm.starter.dto.user.PasswordResetTokenDto;
import com.monogramm.starter.persistence.GenericService;
import com.monogramm.starter.persistence.user.entity.PasswordResetToken;
import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.persistence.user.exception.PasswordResetTokenNotFoundException;

import java.util.UUID;

/**
 * {@link PasswordResetToken} service interface.
 * 
 * @author madmath03
 */
public interface PasswordResetTokenService
    extends GenericService<PasswordResetToken, PasswordResetTokenDto> {

  @Override
  PasswordResetTokenBridge getBridge();

  /**
   * Find a token through its token code and user.
   * 
   * @param user the user to search.
   * @param code the token code to search.
   * 
   * @return the token matching the token code.
   * 
   * @throws PasswordResetTokenNotFoundException if no token matches the code in the repository.
   */
  PasswordResetToken findByUserAndCode(final User user, final String code);

  /**
   * Find a token through its token code and user id.
   * 
   * @param userId the user id to search.
   * @param code the token code to search.
   * 
   * @return the token matching the token code.
   * 
   * @throws PasswordResetTokenNotFoundException if no token matches the code in the repository.
   */
  PasswordResetToken findByUserAndCode(final UUID userId, final String code);

}
