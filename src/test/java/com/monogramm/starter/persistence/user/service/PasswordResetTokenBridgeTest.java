/*
 * Creation by madmath03 the 2017-12-20.
 */

package com.monogramm.starter.persistence.user.service;

import com.monogramm.starter.dto.user.PasswordResetTokenDto;
import com.monogramm.starter.persistence.AbstractTokenBridgeTest;
import com.monogramm.starter.persistence.user.dao.IUserRepository;
import com.monogramm.starter.persistence.user.entity.PasswordResetToken;

/**
 * {@link PasswordResetTokenBridge} Unit Test.
 * 
 * @author madmath03
 */
public class PasswordResetTokenBridgeTest extends
    AbstractTokenBridgeTest<PasswordResetToken, PasswordResetTokenDto, PasswordResetTokenBridge> {

  @Override
  protected PasswordResetTokenBridge buildTestBridge() {
    return new PasswordResetTokenBridge();
  }

  @Override
  protected PasswordResetTokenBridge buildTestBridge(IUserRepository userRepository) {
    return new PasswordResetTokenBridge(userRepository);
  }

}
