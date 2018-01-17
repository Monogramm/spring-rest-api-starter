/*
 * Creation by madmath03 the 2017-12-20.
 */

package com.monogramm.starter.persistence.user.entity;

import com.monogramm.starter.persistence.AbstractToken;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Password Reset Token.
 * 
 * @see <a href="http://www.baeldung.com/spring-security-registration-i-forgot-my-password">Spring
 *      Security – Reset Your Password</a>
 * 
 * @author madmath03
 */
@Entity
@Table(name = "password_reset")
public class PasswordResetToken extends AbstractToken {
  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = -7761234938723607985L;

  /**
   * Get a new {@link PasswordResetTokenBuilder}.
   *
   * @return a new {@link PasswordResetTokenBuilder}.
   */
  public static PasswordResetTokenBuilder builder() {
    return new PasswordResetTokenBuilder();
  }

  /**
   * Get a new {@link PasswordResetTokenBuilder}.
   * 
   * @param token the token of your record being built.
   * @param expiryDate the expiry date of your record being built.
   *
   * @return a new {@link PasswordResetTokenBuilder}.
   */
  public static PasswordResetTokenBuilder builder(final String token, final Date expiryDate) {
    return new PasswordResetTokenBuilder(token, expiryDate);
  }

  /**
   * Create a {@link PasswordResetToken}.
   * 
   */
  public PasswordResetToken() {
    super();
  }

  /**
   * Create a {@link PasswordResetToken}.
   * 
   * @param token the verification token.
   * @param expiryDate the verification token expiry date.
   */
  public PasswordResetToken(String token, Date expiryDate) {
    super(token, expiryDate);
  }

  /**
   * Create a {@link PasswordResetToken}.
   * 
   * @param other the other {@link VerificationToken} to copy.
   */
  public PasswordResetToken(PasswordResetToken other) {
    super(other);
  }

  /**
   * A functional programming password reset token builder.
   * 
   * @author madmath03
   */
  public static class PasswordResetTokenBuilder
      extends AbstractToken.AbstractTokenBuilder<PasswordResetToken> {

    /**
     * Create a {@link PasswordResetTokenBuilder}.
     *
     */
    private PasswordResetTokenBuilder() {
      super();
    }

    /**
     * Create a {@link PasswordResetTokenBuilder}.
     * 
     * @param token the token of your record being built.
     * @param expiryDate the expiry date of your record being built.
     */
    private PasswordResetTokenBuilder(String token, Date expiryDate) {
      super(token, expiryDate);
    }

    @Override
    protected final PasswordResetToken buildEntity() {
      return new PasswordResetToken();
    }
  }

}
