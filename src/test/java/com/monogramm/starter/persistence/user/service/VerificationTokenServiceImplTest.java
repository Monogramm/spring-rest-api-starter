/*
 * Creation by madmath03 the 2017-12-18.
 */

package com.monogramm.starter.persistence.user.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.monogramm.starter.dto.user.VerificationTokenDto;
import com.monogramm.starter.persistence.AbstractGenericServiceTest;
import com.monogramm.starter.persistence.EntityNotFoundException;
import com.monogramm.starter.persistence.parameter.entity.Parameter;
import com.monogramm.starter.persistence.parameter.service.ParameterServiceImpl;
import com.monogramm.starter.persistence.user.dao.VerificationTokenRepository;
import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.persistence.user.entity.VerificationToken;
import com.monogramm.starter.persistence.user.exception.VerificationTokenNotFoundException;

import java.util.Date;
import java.util.UUID;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * {@link VerificationTokenServiceImpl} Unit Test.
 * 
 * @author madmath03
 */
public class VerificationTokenServiceImplTest extends
    AbstractGenericServiceTest<VerificationToken, VerificationTokenDto, VerificationTokenServiceImpl> {

  private static final UUID ID = UUID.randomUUID();
  private static final String TOKEN = VerificationTokenServiceImplTest.class.getSimpleName();
  private static final Date DUMMY_EXPIRY_DATE = new Date();
  private static final UUID USER_ID = UUID.randomUUID();
  private static final User USER = User.builder().id(USER_ID).build();

  @Override
  protected VerificationTokenServiceImpl buildTestService() {
    return new VerificationTokenServiceImpl(getMockRepository(), getMockUserRepository(),
        getMockAuthenticationFacade());
  }

  @Override
  protected VerificationTokenRepository buildMockRepository() {
    return mock(VerificationTokenRepository.class);
  }

  @Override
  public VerificationTokenRepository getMockRepository() {
    return (VerificationTokenRepository) super.getMockRepository();
  }

  @Override
  protected VerificationToken buildTestEntity() {
    return VerificationToken.builder(TOKEN, DUMMY_EXPIRY_DATE).user(USER).id(ID).build();
  }

  @Override
  protected EntityNotFoundException buildEntityNotFoundException() {
    return new VerificationTokenNotFoundException();
  }

  @Override
  @Test
  public void testExists() {
    final VerificationToken model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getCode())).thenReturn(true);

    assertTrue(getService().exists(model));

    verify(getMockRepository(), times(1)).exists(model.getId(), model.getCode());
    verifyNoMoreInteractions(getMockRepository());
  }

  @Override
  @Test
  public void testExistsNotFound() {
    final VerificationToken model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getCode())).thenReturn(false);

    assertFalse(getService().exists(model));

    verify(getMockRepository(), times(1)).exists(model.getId(), model.getCode());
    verifyNoMoreInteractions(getMockRepository());
  }

  /**
   * Test method for {@link ParameterServiceImpl#add(Parameter)}.
   */
  @Override
  @Test
  public void testAdd() {
    final VerificationToken model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getCode())).thenReturn(false);

    getService().add(model);

    ArgumentCaptor<VerificationToken> parameterArgument =
        ArgumentCaptor.forClass(VerificationToken.class);
    verify(getMockRepository(), times(1)).exists(model.getId(), model.getCode());
    verify(getMockRepository(), times(1)).add(parameterArgument.capture());
    verifyNoMoreInteractions(getMockRepository());

    final VerificationToken actual = parameterArgument.getValue();

    assertThat(actual.getCode(), is(model.getCode()));
  }

  /**
   * Test method for {@link ParameterServiceImpl#add(Parameter)}.
   */
  @Override
  @Test
  public void testAddAlreadyExists() {
    final VerificationToken model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getCode())).thenReturn(true);

    getService().add(model);

    verify(getMockRepository(), times(1)).exists(model.getId(), model.getCode());
    verifyNoMoreInteractions(getMockRepository());
  }

  /**
   * Test method for {@link VerificationTokenServiceImpl#findByUserAndCode(UUID, String)}.
   * 
   * @throws VerificationTokenNotFoundException if the verificationToken is not found.
   */
  @Test
  public void testFindByUserAndCode() {
    final VerificationToken model = this.buildTestEntity();

    when(getMockRepository().findByUserAndCode(USER_ID, TOKEN)).thenReturn(model);

    final VerificationToken actual = getService().findByUserAndCode(USER_ID, TOKEN);

    verify(getMockRepository(), times(1)).findByUserAndCode(USER_ID, TOKEN);
    verifyNoMoreInteractions(getMockRepository());

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link VerificationTokenServiceImpl#findByUserAndCode(UUID, String)}.
   * 
   * @throws VerificationTokenNotFoundException if the verificationToken is not found.
   */
  @Test
  public void testFindByUserAndCodeNotFound() {
    when(getMockRepository().findByUserAndCode(USER_ID, TOKEN)).thenReturn(null);

    final VerificationToken actual = getService().findByUserAndCode(USER_ID, TOKEN);

    verify(getMockRepository(), times(1)).findByUserAndCode(USER_ID, TOKEN);
    verifyNoMoreInteractions(getMockRepository());

    assertNull(actual);
  }

  /**
   * Test method for {@link VerificationTokenServiceImpl#findByUserAndCode(User, String)}.
   * 
   * @throws VerificationTokenNotFoundException if the verificationToken is not found.
   */
  @Test
  public void testFindByUserAndTokenUserNotFound() {
    when(getMockRepository().findByUserAndCode(USER, TOKEN)).thenReturn(null);

    final VerificationToken actual = getService().findByUserAndCode(USER, TOKEN);

    verify(getMockRepository(), times(1)).findByUserAndCode(USER, TOKEN);
    verifyNoMoreInteractions(getMockRepository());

    assertNull(actual);
  }

  /**
   * Test method for {@link VerificationTokenServiceImpl#findByUserAndCode(UUID, String)}.
   * 
   * @throws VerificationTokenNotFoundException if the verificationToken is not found.
   */
  @Test(expected = VerificationTokenNotFoundException.class)
  public void testFindByUserAndTokenVerificationTokenNotFoundException() {
    when(getMockRepository().findByUserAndCode(USER_ID, TOKEN))
        .thenThrow(new VerificationTokenNotFoundException());

    getService().findByUserAndCode(USER_ID, TOKEN);
  }

}
