/*
 * Creation by madmath03 the 2017-12-20.
 */

package com.monogramm.starter.persistence;

import com.monogramm.starter.persistence.user.entity.User;

import java.util.Date;
import java.util.Objects;
import java.util.Random;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

/**
 * AbstractToken.
 * 
 * @author madmath03
 */
@MappedSuperclass
public abstract class AbstractToken extends AbstractGenericEntity {
  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = -3530907941489438288L;

  /**
   * Default duration in minutes before expiration of a verification code.
   */
  public static final short EXPIRATION = 60 * 24;
  /**
   * Minutes to Milliseconds.
   */
  private static final long MIN_TO_MS = 60_000;

  /**
   * Token property.
   */
  public static final String TOKEN_PROPERTY = "code";
  /**
   * Token maximum length.
   */
  public static final int TOKEN_MAX_LENGTH = 16;

  /**
   * Expiry date property.
   */
  public static final String EXPIRY_PROPERTY = "expiry_date";

  /**
   * User id property.
   */
  public static final String USER_PROPERTY = "user_id";
  /**
   * User id field.
   */
  public static final String USER_FIELD = "user";

  /**
   * Token default length.
   */
  public static final int DEFAULT_LENGTH_TOKEN = 6;

  private static final char[] READABLE_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'P', 'R', 'U', 'V', 'X', 'Y', 'Z'};

  /**
   * Create a random string with only "<em>readable</em>" characters that will not be confused with
   * others.
   * 
   * <p>
   * Characters with a resemblance are omitted. For instance, {@code 1} and {@code I} may be
   * mistaken. Only upper case letters are used.
   * </p>
   * 
   * @param length length of the random string.
   * 
   * @return a random string.
   * 
   * @throws IllegalArgumentException if {@code length} of requested token is negative.
   */
  public static String createRandomToken(final int length) {
    if (length < 0) {
      throw new IllegalArgumentException("Length must be positive.");
    }
    final Random random = new Random();

    final StringBuilder builder = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      int randomIndex = random.nextInt(READABLE_CHARS.length);
      builder.append(READABLE_CHARS[randomIndex]);
    }

    return builder.toString();
  }

  /**
   * Create an expiry date with the default expiration time.
   * 
   * @see #EXPIRATION
   * 
   * @return an expiry date with the default expiration time.
   */
  public static Date calculateExpiryDate() {
    return calculateExpiryDate(EXPIRATION);
  }

  /**
   * Create an expiry date with the given expiration time in minutes.
   * 
   * @param expiryTimeInMinutes expiration time in minutes.
   * 
   * @return an expiry date with the default expiration time.
   */
  public static Date calculateExpiryDate(long expiryTimeInMinutes) {
    return new Date(System.currentTimeMillis() + expiryTimeInMinutes * MIN_TO_MS);
  }

  /**
   * The verification token.
   */
  @Column(name = TOKEN_PROPERTY, nullable = false, length = TOKEN_MAX_LENGTH)
  private String code;

  /**
   * The verification token expiry date.
   */
  @Column(name = EXPIRY_PROPERTY, nullable = false)
  private Date expiryDate;

  /**
   * Foreign key (relation) to the user\'s account.
   */
  @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = USER_PROPERTY)
  private User user;

  /**
   * Create a {@link AbstractToken}.
   * 
   */
  public AbstractToken() {
    super();

    this.code = AbstractToken.createRandomToken(DEFAULT_LENGTH_TOKEN);
    this.expiryDate = AbstractToken.calculateExpiryDate();
  }

  /**
   * Create a {@link AbstractToken}.
   * 
   * @param token the verification token.
   * @param expiryDate the verification token expiry date.
   */
  public AbstractToken(String token, Date expiryDate) {
    super();

    this.code = token;
    this.expiryDate = expiryDate;
  }

  /**
   * Create a copy of a {@link AbstractToken}.
   * 
   * @param other the other {@link AbstractToken} to copy.
   * 
   * @throws NullPointerException if the {@code other} entity is @{code null}.
   */
  public AbstractToken(AbstractToken other) {
    super(other);

    this.code = other.code;
    this.expiryDate = other.expiryDate;
    this.user = other.user;
  }

  /**
   * Get the {@link #code}.
   * 
   * @return the {@link #code}.
   */
  public String getCode() {
    return code;
  }

  /**
   * Set the {@link #code}.
   * 
   * @param code the {@link #code} to set.
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Get the {@link #expiryDate}.
   * 
   * @return the {@link #expiryDate}.
   */
  public Date getExpiryDate() {
    return expiryDate;
  }

  /**
   * Set the {@link #expiryDate}.
   * 
   * @param expiryDate the {@link #expiryDate} to set.
   */
  public void setExpiryDate(Date expiryDate) {
    this.expiryDate = expiryDate;
  }

  /**
   * Get the {@link #user}.
   * 
   * @return the {@link #user}.
   */
  public User getUser() {
    return user;
  }

  /**
   * Set the {@link #user}.
   * 
   * @param user the {@link #user} to set.
   */
  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public <T extends AbstractGenericEntity> void update(T entity) {
    super.update(entity);

    if (entity instanceof AbstractToken) {
      final AbstractToken genericToken = (AbstractToken) entity;

      this.setCode(genericToken.getCode());
      this.setExpiryDate(genericToken.getExpiryDate());
      this.setUser(genericToken.getUser());
    }
  }

  @Override
  public int hashCode() {
    int hash = super.hashCode();

    if (this.getCode() != null) {
      hash = hash * 31 + this.getCode().hashCode();
    }

    if (this.getExpiryDate() != null) {
      hash = hash * 31 + this.getExpiryDate().hashCode();
    }

    return hash;
  }

  @Override
  public boolean equals(final Object obj) {
    boolean equals = super.equals(obj);

    if (equals && obj instanceof AbstractToken) {
      final AbstractToken other = (AbstractToken) obj;
      equals = Objects.equals(getCode(), other.getCode());

      if (equals) {
        equals = Objects.equals(getExpiryDate(), other.getExpiryDate());
      }
    }

    return equals;
  }

  /**
   * A functional programming token builder.
   * 
   * @author madmath03
   */
  public abstract static class AbstractTokenBuilder<T extends AbstractToken>
      extends AbstractGenericEntity.Builder<T> {

    /**
     * Create a {@link AbstractTokenBuilder}.
     *
     */
    protected AbstractTokenBuilder() {
      super();
    }

    /**
     * Create a {@link AbstractTokenBuilder}.
     * 
     * @param token the token of your record being built.
     * @param expiryDate the expiry date of your record being built.
     */
    protected AbstractTokenBuilder(final String token, final Date expiryDate) {
      this();
      this.code(token).expiryDate(expiryDate);
    }

    /**
     * Set the token and return the builder.
     * 
     * @see AbstractToken#setCode(String)
     * 
     * @param code the code of your record being built.
     * 
     * @return the builder.
     */
    public AbstractTokenBuilder<T> code(final String code) {
      this.getEntity().setCode(code);
      return this;
    }

    /**
     * Set the expiry date and return the builder.
     * 
     * @see AbstractToken#setExpiryDate(Date)
     * 
     * @param expiryDate the expiry date of your record being built.
     * 
     * @return the builder.
     */
    public AbstractTokenBuilder<T> expiryDate(final Date expiryDate) {
      this.getEntity().setExpiryDate(expiryDate);
      return this;
    }

    /**
     * Set the user and return the builder.
     * 
     * @see AbstractToken#setUser(User)
     * 
     * @param user the user of your record being built.
     * 
     * @return the builder.
     */
    public AbstractTokenBuilder<T> user(final User user) {
      this.getEntity().setUser(user);
      return this;
    }
  }

}
