/*
 * Creation by madmath03 the 2017-12-18.
 */

package com.monogramm.starter.persistence.user.dao;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import com.monogramm.starter.persistence.AbstractGenericRepositoryIT;
import com.monogramm.starter.persistence.user.entity.VerificationToken;
import com.monogramm.starter.persistence.user.exception.VerificationTokenNotFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

/**
 * {@link IVerificationTokenRepository} Integration Test.
 * 
 * @author madmath03
 */
public class IVerificationTokenRepositoryIT
    extends AbstractGenericRepositoryIT<VerificationToken, IVerificationTokenRepository> {

  private static final String TOKEN = "Foo";
  private static final Date DUMMY_EXPIRY_DATE = new Date();

  @Override
  protected VerificationToken buildTestEntity() {
    return VerificationToken.builder(TOKEN, DUMMY_EXPIRY_DATE).user(owner).build();
  }

  /**
   * Test method for {@link IVerificationTokenRepository#findAll()}.
   */
  @Override
  @Test
  public void testFindAll() {
    final List<VerificationToken> models = new ArrayList<>();

    final List<VerificationToken> actual = getRepository().findAll();

    assertThat(actual, is(models));
  }

  /**
   * Test method for {@link IVerificationTokenRepository#findByUserAndCode(UUID, String)}.
   * 
   * @throws VerificationTokenNotFoundException if the verificationToken is not found.
   */
  @Test
  public void testFindByUserAndToken() {
    final VerificationToken model = this.buildTestEntity();
    getRepository().add(model);

    final VerificationToken actual =
        getRepository().findByUserAndCode(owner.getId(), model.getCode());

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link IVerificationTokenRepository#findByUserAndCode(UUID, String)}.
   * 
   * @throws VerificationTokenNotFoundException if the verificationToken is not found.
   */
  @Test
  public void testFindByUserAndTokenNoResult() {
    assertNull(getRepository().findByUserAndCode(owner.getId(), TOKEN));
  }

  /**
   * Test method for {@link IVerificationTokenRepository#findByNameIgnoreCase(java.lang.String)}.
   */
  @Test
  public void testFindByUserAndTokenNonUnique() {
    getRepository()
        .add(VerificationToken.builder(TOKEN + "1", DUMMY_EXPIRY_DATE).user(owner).build());
    getRepository()
        .add(VerificationToken.builder(TOKEN + "2", DUMMY_EXPIRY_DATE).user(owner).build());

    assertNull(getRepository().findByUserAndCode(owner.getId(), TOKEN));
  }

  /**
   * Test method for {@link IVerificationTokenRepository#findByUserAndCode(UUID, String)}.
   * 
   * @throws VerificationTokenNotFoundException if the verificationToken is not found.
   */
  @Test
  public void testFindByUserAndTokenNotFound() {
    assertNull(getRepository().findByUserAndCode(owner, null));
  }

  /**
   * Test method for {@link IVerificationTokenRepository#exists(java.util.UUID, java.lang.String)}.
   */
  @Test
  public void testExistsUuidString() {
    final boolean expected = true;
    final VerificationToken model = this.buildTestEntity();
    final List<VerificationToken> models = new ArrayList<>(1);
    models.add(model);
    getRepository().save(models);

    final boolean actual = getRepository().exists(model.getId(), model.getCode());

    assertThat(actual, is(expected));
  }

  /**
   * Test method for {@link IVerificationTokenRepository#exists(java.util.UUID, java.lang.String)}.
   */
  @Test
  public void testExistsUuidStringNotFound() {
    final boolean expected = false;

    final boolean actual = getRepository().exists(RANDOM_ID, TOKEN);

    assertThat(actual, is(expected));
  }

}
