/*
 * Creation by madmath03 the 2017-12-18.
 */

package com.monogramm.starter.persistence.user.service;

import com.monogramm.starter.dto.user.VerificationTokenDto;
import com.monogramm.starter.persistence.AbstractTokenBridgeTest;
import com.monogramm.starter.persistence.user.dao.IUserRepository;
import com.monogramm.starter.persistence.user.entity.VerificationToken;

/**
 * {@link VerificationTokenBridge} Unit Test.
 * 
 * @author madmath03
 */
public class VerificationTokenBridgeTest extends
    AbstractTokenBridgeTest<VerificationToken, VerificationTokenDto, VerificationTokenBridge> {

  @Override
  protected VerificationTokenBridge buildTestBridge() {
    return new VerificationTokenBridge();
  }

  @Override
  protected VerificationTokenBridge buildTestBridge(IUserRepository userRepository) {
    return new VerificationTokenBridge(userRepository);
  }

}
