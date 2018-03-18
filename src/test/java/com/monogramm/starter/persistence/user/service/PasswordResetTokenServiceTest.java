/*
 * Creation by madmath03 the 2017-12-20.
 */

package com.monogramm.starter.persistence.user.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.monogramm.starter.dto.user.PasswordResetTokenDto;
import com.monogramm.starter.persistence.GenericServiceTest;
import com.monogramm.starter.persistence.user.dao.IPasswordResetTokenRepository;
import com.monogramm.starter.persistence.user.dao.IUserRepository;
import com.monogramm.starter.persistence.user.entity.PasswordResetToken;
import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.persistence.user.exception.PasswordResetTokenNotFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 * {@link PasswordResetTokenService} Unit Test.
 * 
 * @author madmath03
 */
public class PasswordResetTokenServiceTest extends
    GenericServiceTest<PasswordResetToken, PasswordResetTokenDto, PasswordResetTokenService> {

  private static final UUID ID = UUID.randomUUID();
  private static final String TOKEN = "Foo";
  private static final Date DUMMY_EXPIRY_DATE = new Date();
  private static final UUID USER_ID = UUID.randomUUID();
  private static final User USER = User.builder().id(USER_ID).build();

  private IPasswordResetTokenRepository passwordResetTokenDao;
  private IUserRepository userDao;

  private PasswordResetTokenService service;

  @Override
  protected PasswordResetTokenService getService() {
    return service;
  }

  /**
   * @throws java.lang.Exception if the test setup crashes.
   */
  @Before
  public void setUp() throws Exception {
    passwordResetTokenDao = mock(IPasswordResetTokenRepository.class);
    userDao = mock(IUserRepository.class);

    service = new PasswordResetTokenService(passwordResetTokenDao, userDao);
  }

  /**
   * @throws java.lang.Exception if the test cleanup crashes.
   */
  @After
  public void tearDown() throws Exception {
    Mockito.reset(passwordResetTokenDao);
    Mockito.reset(userDao);
  }

  /**
   * Test method for {@link PasswordResetTokenService#getBridge()}.
   */
  @Test
  public void testGetBridge() {
    assertNotNull(service.getBridge());
  }

  /**
   * Test method for {@link PasswordResetTokenService#findById(java.util.UUID)}.
   */
  @Test
  public void testFindById() {
    final PasswordResetToken model = new PasswordResetToken(TOKEN, DUMMY_EXPIRY_DATE);
    model.setId(ID);
    model.setUser(USER);

    when(passwordResetTokenDao.findById(ID)).thenReturn(model);

    final PasswordResetToken actual = service.findById(ID);

    verify(passwordResetTokenDao, times(1)).findById(ID);
    verifyNoMoreInteractions(passwordResetTokenDao);

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link PasswordResetTokenService#findById(java.util.UUID)}.
   */
  @Test
  public void testFindByIdNotFound() {
    when(passwordResetTokenDao.findById(ID)).thenReturn(null);

    final PasswordResetToken actual = service.findById(ID);

    verify(passwordResetTokenDao, times(1)).findById(ID);
    verifyNoMoreInteractions(passwordResetTokenDao);

    assertNull(actual);
  }

  /**
   * Test method for {@link PasswordResetTokenService#findByUserAndCode(User, String)}.
   * 
   * @throws PasswordResetTokenNotFoundException if the passwordResetToken is not found.
   */
  @Test
  public void testFindByUserAndTokenUserString() {
    final PasswordResetToken model = new PasswordResetToken(TOKEN, DUMMY_EXPIRY_DATE);
    model.setId(ID);
    model.setUser(USER);
    final User data = User.builder().id(ID).build();

    when(passwordResetTokenDao.findByUserAndCode(data, TOKEN)).thenReturn(model);

    final PasswordResetToken actual = service.findByUserAndCode(data, TOKEN);

    verify(passwordResetTokenDao, times(1)).findByUserAndCode(data, TOKEN);
    verifyNoMoreInteractions(passwordResetTokenDao);

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link PasswordResetTokenService#findByUserAndCode(UUID, String)}.
   * 
   * @throws PasswordResetTokenNotFoundException if the passwordResetToken is not found.
   */
  @Test
  public void testFindByUserAndTokenUuidString() {
    final PasswordResetToken model = new PasswordResetToken(TOKEN, DUMMY_EXPIRY_DATE);
    model.setId(ID);
    model.setUser(USER);

    when(passwordResetTokenDao.findByUserAndCode(USER_ID, TOKEN)).thenReturn(model);

    final PasswordResetToken actual = service.findByUserAndCode(USER_ID, TOKEN);

    verify(passwordResetTokenDao, times(1)).findByUserAndCode(USER_ID, TOKEN);
    verifyNoMoreInteractions(passwordResetTokenDao);

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link PasswordResetTokenService#findByUserAndCode(UUID, String)}.
   * 
   * @throws PasswordResetTokenNotFoundException if the passwordResetToken is not found.
   */
  @Test
  public void testFindByUserAndTokenNotFound() {
    when(passwordResetTokenDao.findByUserAndCode(USER_ID, TOKEN)).thenReturn(null);

    final PasswordResetToken actual = service.findByUserAndCode(USER_ID, TOKEN);

    verify(passwordResetTokenDao, times(1)).findByUserAndCode(USER_ID, TOKEN);
    verifyNoMoreInteractions(passwordResetTokenDao);

    assertNull(actual);
  }

  /**
   * Test method for {@link PasswordResetTokenService#findByUserAndCode(UUID, String)}.
   * 
   * @throws PasswordResetTokenNotFoundException if the passwordResetToken is not found.
   */
  @Test(expected = PasswordResetTokenNotFoundException.class)
  public void testFindByUserAndTokenPasswordResetTokenNotFoundException() {
    when(passwordResetTokenDao.findByUserAndCode(USER_ID, TOKEN))
        .thenThrow(new PasswordResetTokenNotFoundException());

    service.findByUserAndCode(USER_ID, TOKEN);
  }

  /**
   * Test method for {@link PasswordResetTokenService#findAll()}.
   */
  @Test
  public void testFindAll() {
    final List<PasswordResetToken> models = new ArrayList<>();
    when(passwordResetTokenDao.findAll()).thenReturn(models);

    final List<PasswordResetToken> actual = service.findAll();

    verify(passwordResetTokenDao, times(1)).findAll();
    verifyNoMoreInteractions(passwordResetTokenDao);

    assertThat(actual, is(models));
  }

  /**
   * Test method for
   * {@link PasswordResetTokenService#add(com.monogramm.starter.persistence.passwordResetToken.entity.PasswordResetToken)}.
   */
  @Test
  public void testAddPasswordResetToken() {
    final PasswordResetToken model = new PasswordResetToken(TOKEN, DUMMY_EXPIRY_DATE);

    service.add(model);

    ArgumentCaptor<PasswordResetToken> passwordResetTokenArgument =
        ArgumentCaptor.forClass(PasswordResetToken.class);
    verify(passwordResetTokenDao, times(1)).exists(null, TOKEN);
    verify(passwordResetTokenDao, times(1)).add(passwordResetTokenArgument.capture());
    verifyNoMoreInteractions(passwordResetTokenDao);

    final PasswordResetToken actual = passwordResetTokenArgument.getValue();

    assertNull(actual.getId());
    assertThat(actual.getCode(), is(model.getCode()));
    assertThat(actual.getExpiryDate(), is(model.getExpiryDate()));
  }

  /**
   * Test method for
   * {@link PasswordResetTokenService#add(com.monogramm.starter.persistence.passwordResetToken.entity.PasswordResetToken)}.
   */
  @Test
  public void testAddPasswordResetTokenAlreadyExists() {
    final PasswordResetToken model = new PasswordResetToken(TOKEN, DUMMY_EXPIRY_DATE);

    when(passwordResetTokenDao.exists(null, TOKEN)).thenReturn(true);

    service.add(model);

    verify(passwordResetTokenDao, times(1)).exists(null, TOKEN);
    verifyNoMoreInteractions(passwordResetTokenDao);
  }

  /**
   * Test method for
   * {@link PasswordResetTokenService#update(com.monogramm.starter.persistence.passwordResetToken.entity.PasswordResetToken)}.
   * 
   * @throws PasswordResetTokenNotFoundException if the passwordResetToken is not found.
   */
  @Test
  public void testUpdatePasswordResetToken() {
    final PasswordResetToken model = new PasswordResetToken(TOKEN, DUMMY_EXPIRY_DATE);
    model.setId(ID);

    when(passwordResetTokenDao.update(model)).thenReturn(model);

    final PasswordResetToken actual = service.update(model);

    verify(passwordResetTokenDao, times(1)).update(model);
    verifyNoMoreInteractions(passwordResetTokenDao);

    assertThat(actual, is(model));
  }

  /**
   * Test method for
   * {@link PasswordResetTokenService#update(com.monogramm.starter.persistence.passwordResetToken.entity.PasswordResetToken)}.
   * 
   * @throws PasswordResetTokenNotFoundException if the passwordResetToken is not found.
   */
  @Test(expected = PasswordResetTokenNotFoundException.class)
  public void testUpdatePasswordResetTokenNotFound() {
    final PasswordResetToken model = new PasswordResetToken(TOKEN, DUMMY_EXPIRY_DATE);
    model.setId(ID);

    when(passwordResetTokenDao.update(model)).thenReturn(null);

    service.update(model);
  }

  /**
   * Test method for
   * {@link PasswordResetTokenService#update(com.monogramm.starter.persistence.passwordResetToken.entity.PasswordResetToken)}.
   * 
   * @throws PasswordResetTokenNotFoundException if the passwordResetToken is not found.
   */
  @Test(expected = PasswordResetTokenNotFoundException.class)
  public void testUpdatePasswordResetTokenNotFoundException() {
    final PasswordResetToken model = new PasswordResetToken(TOKEN, DUMMY_EXPIRY_DATE);
    model.setId(ID);

    when(passwordResetTokenDao.update(model)).thenThrow(new PasswordResetTokenNotFoundException());

    service.update(model);
  }

  /**
   * Test method for {@link PasswordResetTokenService#deleteById(java.util.UUID)}.
   * 
   * @throws PasswordResetTokenNotFoundException if the passwordResetToken is not found.
   */
  @Test
  public void testDeletePasswordResetToken() {
    when(passwordResetTokenDao.deleteById(ID)).thenReturn(1);

    service.deleteById(ID);

    verify(passwordResetTokenDao, times(1)).deleteById(ID);
    verifyNoMoreInteractions(passwordResetTokenDao);
  }

  /**
   * Test method for {@link PasswordResetTokenService#deleteById(java.util.UUID)}.
   * 
   * @throws PasswordResetTokenNotFoundException if the passwordResetToken is not found.
   */
  @Test(expected = PasswordResetTokenNotFoundException.class)
  public void testDeletePasswordResetTokenNotFound() {
    when(passwordResetTokenDao.deleteById(ID)).thenReturn(null);

    service.deleteById(ID);
  }

  /**
   * Test method for {@link PasswordResetTokenService#deleteById(java.util.UUID)}.
   * 
   * @throws PasswordResetTokenNotFoundException if the passwordResetToken is not found.
   */
  @Test(expected = PasswordResetTokenNotFoundException.class)
  public void testDeletePasswordResetTokenNotFoundException() {
    when(passwordResetTokenDao.deleteById(ID)).thenThrow(new PasswordResetTokenNotFoundException());

    service.deleteById(ID);
  }

}
