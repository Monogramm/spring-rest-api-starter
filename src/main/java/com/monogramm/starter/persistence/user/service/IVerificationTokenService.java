/*
 * Creation by madmath03 the 2017-12-18.
 */

package com.monogramm.starter.persistence.user.service;

import com.monogramm.starter.dto.user.VerificationTokenDto;
import com.monogramm.starter.persistence.GenericService;
import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.persistence.user.entity.VerificationToken;
import com.monogramm.starter.persistence.user.exception.VerificationTokenNotFoundException;

import java.util.UUID;

public interface IVerificationTokenService
    extends GenericService<VerificationToken, VerificationTokenDto> {

  @Override
  VerificationTokenBridge getBridge();

  /**
   * Find a token through its token code and user.
   * 
   * @param user the user to search.
   * @param code the token code to search.
   * 
   * @return the token matching the token code.
   * 
   * @throws VerificationTokenNotFoundException if no token matches the code in the repository.
   */
  VerificationToken findByUserAndCode(final User user, final String code);

  /**
   * Find an token through its token code and user id.
   * 
   * @param userId the user id to search.
   * @param code the token code to search.
   * 
   * @return the token matching the token code.
   * 
   * @throws VerificationTokenNotFoundException if no token matches the code in the repository.
   */
  VerificationToken findByUserAndCode(final UUID userId, final String code);

}
