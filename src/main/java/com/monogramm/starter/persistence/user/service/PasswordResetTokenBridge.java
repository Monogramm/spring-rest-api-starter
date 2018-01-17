/*
 * Creation by madmath03 the 2017-12-20.
 */

package com.monogramm.starter.persistence.user.service;

import com.monogramm.starter.dto.user.PasswordResetTokenDto;
import com.monogramm.starter.persistence.AbstractTokenBridge;
import com.monogramm.starter.persistence.user.dao.IUserRepository;
import com.monogramm.starter.persistence.user.entity.PasswordResetToken;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Bridge to convert a {@link PasswordResetTokenDto} to a {@link PasswordResetToken} and vice versa.
 * 
 * @author madmath03
 */
public class PasswordResetTokenBridge
    extends AbstractTokenBridge<PasswordResetToken, PasswordResetTokenDto> {

  /**
   * Create a {@link PasswordResetTokenBridge}.
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
  public PasswordResetTokenBridge() {
    super();
  }

  /**
   * Create a {@link PasswordResetTokenBridge}.
   * 
   * @param userRepository repository to lookup users.
   */
  @Autowired
  public PasswordResetTokenBridge(IUserRepository userRepository) {
    super(userRepository);
  }

  @Override
  protected PasswordResetToken buildEntity() {
    return new PasswordResetToken();
  }

  @Override
  protected PasswordResetTokenDto buildDto() {
    return new PasswordResetTokenDto();
  }
}
