/*
 * Creation by madmath03 the 2017-12-20.
 */

package com.monogramm.starter.dto.user;

import com.monogramm.starter.dto.AbstractTokenDtoTest;

/**
 * {@link PasswordResetTokenDto} Unit Test.
 * 
 * @author madmath03
 */
public class PasswordResetTokenDtoTest extends AbstractTokenDtoTest<PasswordResetTokenDto> {

  @Override
  protected PasswordResetTokenDto buildTestDto(PasswordResetTokenDto other) {
    return new PasswordResetTokenDto(other);
  }

  @Override
  protected PasswordResetTokenDto buildTestDto() {
    return new PasswordResetTokenDto();
  }

}
