/*
 * Creation by madmath03 the 2017-12-20.
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

import com.monogramm.starter.dto.user.PasswordResetTokenDto;
import com.monogramm.starter.persistence.AbstractGenericServiceTest;
import com.monogramm.starter.persistence.EntityNotFoundException;
import com.monogramm.starter.persistence.parameter.entity.Parameter;
import com.monogramm.starter.persistence.parameter.service.ParameterServiceImpl;
import com.monogramm.starter.persistence.user.dao.PasswordResetTokenRepository;
import com.monogramm.starter.persistence.user.entity.PasswordResetToken;
import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.persistence.user.exception.PasswordResetTokenNotFoundException;

import java.util.Date;
import java.util.UUID;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * {@link PasswordResetTokenServiceImpl} Unit Test.
 * 
 * @author madmath03
 */
public class PasswordResetTokenServiceImplTest extends
    AbstractGenericServiceTest<PasswordResetToken, PasswordResetTokenDto, PasswordResetTokenServiceImpl> {

  private static final UUID ID = UUID.randomUUID();
  private static final String TOKEN = PasswordResetTokenServiceImplTest.class.getSimpleName();
  private static final Date DUMMY_EXPIRY_DATE = new Date();
  private static final UUID USER_ID = UUID.randomUUID();
  private static final User USER = User.builder().id(USER_ID).build();

  @Override
  protected PasswordResetTokenServiceImpl buildTestService() {
    return new PasswordResetTokenServiceImpl(getMockRepository(), getMockUserRepository(),
        getMockAuthenticationFacade());
  }

  @Override
  protected PasswordResetTokenRepository buildMockRepository() {
    return mock(PasswordResetTokenRepository.class);
  }

  @Override
  public PasswordResetTokenRepository getMockRepository() {
    return (PasswordResetTokenRepository) super.getMockRepository();
  }

  @Override
  protected PasswordResetToken buildTestEntity() {
    return PasswordResetToken.builder(TOKEN, DUMMY_EXPIRY_DATE).user(USER).id(ID).build();
  }

  @Override
  protected EntityNotFoundException buildEntityNotFoundException() {
    return new PasswordResetTokenNotFoundException();
  }

  @Override
  @Test
  public void testExists() {
    final PasswordResetToken model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getCode())).thenReturn(true);

    assertTrue(getService().exists(model));

    verify(getMockRepository(), times(1)).exists(model.getId(), model.getCode());
    verifyNoMoreInteractions(getMockRepository());
  }

  @Override
  @Test
  public void testExistsNotFound() {
    final PasswordResetToken model = this.buildTestEntity();

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
    final PasswordResetToken model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getCode())).thenReturn(false);

    getService().add(model);

    ArgumentCaptor<PasswordResetToken> parameterArgument =
        ArgumentCaptor.forClass(PasswordResetToken.class);
    verify(getMockRepository(), times(1)).exists(model.getId(), model.getCode());
    verify(getMockRepository(), times(1)).add(parameterArgument.capture());
    verifyNoMoreInteractions(getMockRepository());

    final PasswordResetToken actual = parameterArgument.getValue();

    assertThat(actual.getCode(), is(model.getCode()));
  }

  /**
   * Test method for {@link ParameterServiceImpl#add(Parameter)}.
   */
  @Override
  @Test
  public void testAddAlreadyExists() {
    final PasswordResetToken model = this.buildTestEntity();

    when(getMockRepository().exists(model.getId(), model.getCode())).thenReturn(true);

    getService().add(model);

    verify(getMockRepository(), times(1)).exists(model.getId(), model.getCode());
    verifyNoMoreInteractions(getMockRepository());
  }

  /**
   * Test method for {@link PasswordResetTokenServiceImpl#findByUserAndCode(UUID, String)}.
   * 
   * @throws PasswordResetTokenNotFoundException if the verificationToken is not found.
   */
  @Test
  public void testFindByUserAndCode() {
    final PasswordResetToken model = this.buildTestEntity();

    when(getMockRepository().findByUserAndCode(USER_ID, TOKEN)).thenReturn(model);

    final PasswordResetToken actual = getService().findByUserAndCode(USER_ID, TOKEN);

    verify(getMockRepository(), times(1)).findByUserAndCode(USER_ID, TOKEN);
    verifyNoMoreInteractions(getMockRepository());

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link PasswordResetTokenServiceImpl#findByUserAndCode(UUID, String)}.
   * 
   * @throws PasswordResetTokenNotFoundException if the verificationToken is not found.
   */
  @Test
  public void testFindByUserAndCodeNotFound() {
    when(getMockRepository().findByUserAndCode(USER_ID, TOKEN)).thenReturn(null);

    final PasswordResetToken actual = getService().findByUserAndCode(USER_ID, TOKEN);

    verify(getMockRepository(), times(1)).findByUserAndCode(USER_ID, TOKEN);
    verifyNoMoreInteractions(getMockRepository());

    assertNull(actual);
  }

  /**
   * Test method for {@link PasswordResetTokenServiceImpl#findByUserAndCode(User, String)}.
   * 
   * @throws PasswordResetTokenNotFoundException if the verificationToken is not found.
   */
  @Test
  public void testFindByUserAndTokenUserNotFound() {
    when(getMockRepository().findByUserAndCode(USER, TOKEN)).thenReturn(null);

    final PasswordResetToken actual = getService().findByUserAndCode(USER, TOKEN);

    verify(getMockRepository(), times(1)).findByUserAndCode(USER, TOKEN);
    verifyNoMoreInteractions(getMockRepository());

    assertNull(actual);
  }

  /**
   * Test method for {@link PasswordResetTokenServiceImpl#findByUserAndCode(UUID, String)}.
   * 
   * @throws PasswordResetTokenNotFoundException if the verificationToken is not found.
   */
  @Test(expected = PasswordResetTokenNotFoundException.class)
  public void testFindByUserAndTokenPasswordResetTokenNotFoundException() {
    when(getMockRepository().findByUserAndCode(USER_ID, TOKEN))
        .thenThrow(new PasswordResetTokenNotFoundException());

    getService().findByUserAndCode(USER_ID, TOKEN);
  }

}
