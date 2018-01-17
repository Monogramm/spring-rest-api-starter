/*
 * Creation by madmath03 the 2017-12-18.
 */

package com.monogramm.starter.persistence.user.service;

import com.monogramm.starter.dto.user.VerificationTokenDto;
import com.monogramm.starter.persistence.AbstractTokenBridge;
import com.monogramm.starter.persistence.user.dao.IUserRepository;
import com.monogramm.starter.persistence.user.entity.VerificationToken;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Bridge to convert a {@link VerificationTokenDto} to a {@link VerificationToken} and vice versa.
 * 
 * @author madmath03
 */
public class VerificationTokenBridge
    extends AbstractTokenBridge<VerificationToken, VerificationTokenDto> {

  /**
   * Create a {@link VerificationTokenBridge}.
   * 
   * <p>
   * <strong>Use with caution:</strong> this constructor will not set the @{code userRepository},
   * preventing any search in the Persistence Storage for the relations objects. This might be
   * dangerous when converting
   * {@link AbstractTokenBridge#toEntity(com.monogramm.starter.dto.AbstractTokenDto)}
   * as no consistency check will be done but it will definitely improve performance.
   * </p>
   * 
   */
  public VerificationTokenBridge() {
    super();
  }

  /**
   * Create a {@link VerificationTokenBridge}.
   * 
   * @param userRepository repository to lookup users.
   */
  @Autowired
  public VerificationTokenBridge(IUserRepository userRepository) {
    super(userRepository);
  }

  @Override
  protected VerificationToken buildEntity() {
    return new VerificationToken();
  }

  @Override
  protected VerificationTokenDto buildDto() {
    return new VerificationTokenDto();
  }
}
