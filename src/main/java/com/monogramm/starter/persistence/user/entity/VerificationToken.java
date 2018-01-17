/*
 * Creation by madmath03 the 2017-12-18.
 */

package com.monogramm.starter.persistence.user.entity;

import com.monogramm.starter.persistence.AbstractGenericEntity;
import com.monogramm.starter.persistence.AbstractToken;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Verification Token.
 * 
 * @see <a href="http://www.baeldung.com/registration-verify-user-by-email">Registration – Activate
 *      a New Account by Email</a>
 * 
 * @author madmath03
 */
@Entity
@Table(name = "user_verification")
public class VerificationToken extends AbstractToken {
  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = -650305735825408964L;

  /**
   * Verification field maximum length.
   */
  public static final int MAX_LENGTH_FIELD = 32;

  /**
   * Verification value maximum length.
   */
  public static final int MAX_LENGTH_VALUE = 512;

  /**
   * Get a new {@link VerificationTokenBuilder}.
   *
   * @return a new {@link VerificationTokenBuilder}.
   */
  public static VerificationTokenBuilder builder() {
    return new VerificationTokenBuilder();
  }

  /**
   * Get a new {@link VerificationTokenBuilder}.
   * 
   * @param token the token of your record being built.
   * @param expiryDate the expiry date of your record being built.
   *
   * @return a new {@link VerificationTokenBuilder}.
   */
  public static VerificationTokenBuilder builder(final String token, final Date expiryDate) {
    return new VerificationTokenBuilder(token, expiryDate);
  }

  /**
   * The verification field.
   */
  @Column(name = "field", nullable = true, length = MAX_LENGTH_FIELD)
  private String field;
  /**
   * The verification field value.
   */
  @Column(name = "value", nullable = true, length = MAX_LENGTH_VALUE)
  private String value;

  /**
   * Create a {@link VerificationToken}.
   * 
   */
  public VerificationToken() {
    super();
  }

  /**
   * Create a {@link VerificationToken}.
   * 
   * @param token the verification token.
   * @param expiryDate the verification token expiry date.
   */
  public VerificationToken(String token, Date expiryDate) {
    this(token, expiryDate, null, null);
  }

  /**
   * Create a {@link VerificationToken}.
   * 
   * @param token the verification token.
   * @param expiryDate the verification token expiry date.
   * @param field the field being verified.
   * @param value the value of the field being verified.
   */
  public VerificationToken(String token, Date expiryDate, String field, String value) {
    super(token, expiryDate);

    this.field = field;
    this.value = value;
  }

  /**
   * Create a copy of a {@link VerificationToken}.
   * 
   * @param other the other {@link VerificationToken} to copy.
   */
  public VerificationToken(VerificationToken other) {
    super(other);
  }

  /**
   * Get the {@link #field}.
   * 
   * @return the {@link #field}.
   */
  public String getField() {
    return field;
  }

  /**
   * Set the {@link #field}.
   * 
   * @param field the {@link #field} to set.
   */
  public void setField(String field) {
    this.field = field;
  }

  /**
   * Get the {@link #value}.
   * 
   * @return the {@link #value}.
   */
  public String getValue() {
    return value;
  }

  /**
   * Set the {@link #value}.
   * 
   * @param value the {@link #value} to set.
   */
  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public <T extends AbstractGenericEntity> void update(T entity) {
    super.update(entity);

    if (entity instanceof VerificationToken) {
      final VerificationToken verificationToken = (VerificationToken) entity;

      this.setField(verificationToken.getField());
      this.setValue(verificationToken.getValue());
    }
  }

  @Override
  public int hashCode() {
    int hash = super.hashCode();

    if (this.getField() == null) {
      hash *= 31;
    } else {
      hash = hash * 31 + this.getField().hashCode();
    }

    if (this.getValue() == null) {
      hash *= 31;
    } else {
      hash = hash * 31 + this.getValue().hashCode();
    }

    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    boolean equals = super.equals(obj);

    if (equals && obj instanceof VerificationToken) {
      final VerificationToken other = (VerificationToken) obj;

      equals = Objects.equals(getField(), other.getField())
          && Objects.equals(getValue(), other.getValue());
    }

    return equals;
  }

  /**
   * A functional programming verification token builder.
   * 
   * @author madmath03
   */
  public static class VerificationTokenBuilder
      extends AbstractToken.AbstractTokenBuilder<VerificationToken> {

    /**
     * Create a {@link VerificationTokenBuilder}.
     *
     */
    private VerificationTokenBuilder() {
      super();
    }

    /**
     * Create a {@link VerificationTokenBuilder}.
     * 
     * @param token the token of your record being built.
     * @param expiryDate the expiry date of your record being built.
     */
    private VerificationTokenBuilder(String token, Date expiryDate) {
      super(token, expiryDate);
    }

    @Override
    protected final VerificationToken buildEntity() {
      return new VerificationToken();
    }

    /**
     * Set the field and return the builder.
     * 
     * @see VerificationToken#setField(String)
     * 
     * @param field the field of your record being built.
     * 
     * @return the builder.
     */
    public VerificationTokenBuilder field(final String field) {
      this.getEntity().setField(field);
      return this;
    }

    /**
     * Set the value and return the builder.
     * 
     * @see VerificationToken#setValue(String)
     * 
     * @param value the value of your record being built.
     * 
     * @return the builder.
     */
    public VerificationTokenBuilder value(final String value) {
      this.getEntity().setValue(value);
      return this;
    }
  }

}
