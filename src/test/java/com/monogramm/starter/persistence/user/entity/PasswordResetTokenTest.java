/*
 * Creation by madmath03 the 2017-12-20.
 */

package com.monogramm.starter.persistence.user.entity;

import com.monogramm.starter.persistence.AbstractTokenTest;

import java.util.Date;

/**
 * {@link PasswordResetToken} Unit Test.
 * 
 * @author madmath03
 */
public class PasswordResetTokenTest extends AbstractTokenTest<PasswordResetToken> {

  @Override
  protected PasswordResetToken buildTestEntity(String token, Date expiryDate) {
    return new PasswordResetToken(token, expiryDate);
  }

  @Override
  protected PasswordResetToken buildTestEntity(PasswordResetToken other) {
    return new PasswordResetToken(other);
  }

  @Override
  protected PasswordResetToken buildTestEntity() {
    return new PasswordResetToken();
  }

}
