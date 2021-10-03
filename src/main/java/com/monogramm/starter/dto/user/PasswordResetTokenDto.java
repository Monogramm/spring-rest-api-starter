/*
 * Creation by madmath03 the 2017-12-20.
 */

package com.monogramm.starter.dto.user;

import com.monogramm.starter.dto.AbstractTokenDto;

/**
 * PasswordResetTokenDto.
 * 
 * @author madmath03
 */
public class PasswordResetTokenDto extends AbstractTokenDto {
  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = 2662064376632153095L;

  /**
   * Create a {@link PasswordResetTokenDto}.
   * 
   */
  public PasswordResetTokenDto() {
    super();
  }

  /**
   * Create a copy of a {@link PasswordResetTokenDto}.
   * 
   * @param other the other {@link PasswordResetTokenDto} to copy.
   */
  public PasswordResetTokenDto(PasswordResetTokenDto other) {
    super(other);
  }

}
