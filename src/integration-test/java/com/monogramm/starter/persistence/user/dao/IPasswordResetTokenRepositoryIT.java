/*
 * Creation by madmath03 the 2017-12-20.
 */

package com.monogramm.starter.persistence.user.dao;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import com.monogramm.starter.persistence.AbstractGenericRepositoryIT;
import com.monogramm.starter.persistence.user.entity.PasswordResetToken;
import com.monogramm.starter.persistence.user.exception.PasswordResetTokenNotFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

/**
 * {@link IPasswordResetTokenRepository} Integration Test.
 * 
 * @author madmath03
 */
public class IPasswordResetTokenRepositoryIT
    extends AbstractGenericRepositoryIT<PasswordResetToken, IPasswordResetTokenRepository> {

  private static final String TOKEN = "Foo";
  private static final Date DUMMY_EXPIRY_DATE = new Date();

  @Override
  protected PasswordResetToken buildTestEntity() {
    return PasswordResetToken.builder(TOKEN, DUMMY_EXPIRY_DATE).user(owner).build();
  }

  /**
   * Test method for {@link IPasswordResetTokenRepository#findAll()}.
   */
  @Override
  @Test
  public void testFindAll() {
    final List<PasswordResetToken> models = new ArrayList<>();

    final List<PasswordResetToken> actual = getRepository().findAll();

    assertThat(actual, is(models));
  }

  /**
   * Test method for {@link IPasswordResetTokenRepository#findByNameIgnoreCase(String)}.
   * 
   * @throws PasswordResetTokenNotFoundException if the passwordResetToken is not found.
   */
  @Test
  public void testFindByUserAndToken() {
    final PasswordResetToken model = this.buildTestEntity();
    getRepository().add(model);

    final PasswordResetToken actual =
        getRepository().findByUserAndCode(owner.getId(), model.getCode());

    assertThat(actual, is(model));
  }

  /**
   * Test method for
   * {@link IPasswordResetTokenRepository#findByNameIgnoreCase(java.lang.String)}.
   * 
   * @throws PasswordResetTokenNotFoundException if the passwordResetToken is not found.
   */
  @Test
  public void testFindByUserAndTokenNoResult() {
    assertNull(getRepository().findByUserAndCode(owner.getId(), TOKEN));
  }

  /**
   * Test method for
   * {@link IPasswordResetTokenRepository#findByNameIgnoreCase(java.lang.String)}.
   * 
   * @throws PasswordResetTokenNotFoundException if the passwordResetToken is not found.
   */
  @Test
  public void testFindByUserAndTokenNonUnique() {
    getRepository()
        .add(PasswordResetToken.builder(TOKEN + "1", DUMMY_EXPIRY_DATE).user(owner).build());
    getRepository()
        .add(PasswordResetToken.builder(TOKEN + "2", DUMMY_EXPIRY_DATE).user(owner).build());

    assertNull(getRepository().findByUserAndCode(owner.getId(), TOKEN));
  }

  /**
   * Test method for {@link IPasswordResetTokenRepository#findByUserAndCode(UUID, String)}.
   * 
   * @throws PasswordResetTokenNotFoundException if the passwordResetToken is not found.
   */
  @Test
  public void testFindByUserAndTokenNotFound() {
    assertNull(getRepository().findByUserAndCode(owner, null));
  }

  /**
   * Test method for {@link IPasswordResetTokenRepository#exists(java.util.UUID, java.lang.String)}.
   */
  @Test
  public void testExistsUuidString() {
    final boolean expected = true;
    final List<PasswordResetToken> models = new ArrayList<>(1);
    final PasswordResetToken model = this.buildTestEntity();
    models.add(model);
    getRepository().save(models);

    final boolean actual = getRepository().exists(model.getId(), model.getCode());

    assertThat(actual, is(expected));
  }

  /**
   * Test method for {@link IPasswordResetTokenRepository#exists(java.util.UUID, java.lang.String)}.
   */
  @Test
  public void testExistsUuidStringNotFound() {
    final boolean expected = false;

    final boolean actual = getRepository().exists(RANDOM_ID, TOKEN);

    assertThat(actual, is(expected));
  }

}
