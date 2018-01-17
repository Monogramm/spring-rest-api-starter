/*
 * Creation by madmath03 the 2017-12-18.
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

import com.monogramm.starter.dto.user.VerificationTokenDto;
import com.monogramm.starter.persistence.GenericServiceTest;
import com.monogramm.starter.persistence.user.dao.IUserRepository;
import com.monogramm.starter.persistence.user.dao.IVerificationTokenRepository;
import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.persistence.user.entity.VerificationToken;
import com.monogramm.starter.persistence.user.exception.VerificationTokenNotFoundException;

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
 * {@link VerificationTokenService} Unit Test.
 * 
 * @author madmath03
 */
public class VerificationTokenServiceTest
    extends GenericServiceTest<VerificationToken, VerificationTokenDto, VerificationTokenService> {

  private static final UUID ID = UUID.randomUUID();
  private static final String TOKEN = "Foo";
  private static final Date DUMMY_EXPIRY_DATE = new Date();
  private static final UUID USER_ID = UUID.randomUUID();
  private static final User USER = User.builder().id(USER_ID).build();

  private IVerificationTokenRepository verificationTokenDao;
  private IUserRepository userDao;

  private VerificationTokenService service;

  @Override
  protected VerificationTokenService getService() {
    return service;
  }

  /**
   * @throws java.lang.Exception if the test setup crashes.
   */
  @Before
  public void setUp() throws Exception {
    verificationTokenDao = mock(IVerificationTokenRepository.class);
    userDao = mock(IUserRepository.class);

    service = new VerificationTokenService(verificationTokenDao, userDao);
  }

  /**
   * @throws java.lang.Exception if the test cleanup crashes.
   */
  @After
  public void tearDown() throws Exception {
    Mockito.reset(verificationTokenDao);
    Mockito.reset(userDao);
  }

  /**
   * Test method for {@link VerificationTokenService#getBridge()}.
   */
  @Test
  public void testGetBridge() {
    assertNotNull(service.getBridge());
  }

  /**
   * Test method for {@link VerificationTokenService#findById(java.util.UUID)}.
   */
  @Test
  public void testFindById() {
    final VerificationToken model = new VerificationToken(TOKEN, DUMMY_EXPIRY_DATE);
    model.setId(ID);
    model.setUser(USER);

    when(verificationTokenDao.findById(ID)).thenReturn(model);

    final VerificationToken actual = service.findById(ID);

    verify(verificationTokenDao, times(1)).findById(ID);
    verifyNoMoreInteractions(verificationTokenDao);

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link VerificationTokenService#findById(java.util.UUID)}.
   */
  @Test
  public void testFindByIdNotFound() {
    when(verificationTokenDao.findById(ID)).thenReturn(null);

    final VerificationToken actual = service.findById(ID);

    verify(verificationTokenDao, times(1)).findById(ID);
    verifyNoMoreInteractions(verificationTokenDao);

    assertNull(actual);
  }

  /**
   * Test method for {@link VerificationTokenService#findByUserAndCode(UUID, String)}.
   * 
   * @throws VerificationTokenNotFoundException if the verificationToken is not found.
   */
  @Test
  public void testFindByUserAndTokenUUID() {
    final VerificationToken model = new VerificationToken(TOKEN, DUMMY_EXPIRY_DATE);
    model.setId(ID);
    model.setUser(USER);

    when(verificationTokenDao.findByUserAndCode(USER_ID, TOKEN)).thenReturn(model);

    final VerificationToken actual = service.findByUserAndCode(USER_ID, TOKEN);

    verify(verificationTokenDao, times(1)).findByUserAndCode(USER_ID, TOKEN);
    verifyNoMoreInteractions(verificationTokenDao);

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link VerificationTokenService#findByUserAndCode(UUID, String)}.
   * 
   * @throws VerificationTokenNotFoundException if the verificationToken is not found.
   */
  @Test
  public void testFindByUserAndTokenUUIDNotFound() {
    when(verificationTokenDao.findByUserAndCode(USER_ID, TOKEN)).thenReturn(null);

    final VerificationToken actual = service.findByUserAndCode(USER_ID, TOKEN);

    verify(verificationTokenDao, times(1)).findByUserAndCode(USER_ID, TOKEN);
    verifyNoMoreInteractions(verificationTokenDao);

    assertNull(actual);
  }

  /**
   * Test method for {@link VerificationTokenService#findByUserAndCode(User, String)}.
   * 
   * @throws VerificationTokenNotFoundException if the verificationToken is not found.
   */
  @Test
  public void testFindByUserAndTokenUser() {
    final VerificationToken model = new VerificationToken(TOKEN, DUMMY_EXPIRY_DATE);
    model.setId(ID);
    model.setUser(USER);

    when(verificationTokenDao.findByUserAndCode(USER, TOKEN)).thenReturn(model);

    final VerificationToken actual = service.findByUserAndCode(USER, TOKEN);

    verify(verificationTokenDao, times(1)).findByUserAndCode(USER, TOKEN);
    verifyNoMoreInteractions(verificationTokenDao);

    assertThat(actual, is(model));
  }

  /**
   * Test method for {@link VerificationTokenService#findByUserAndCode(User, String)}.
   * 
   * @throws VerificationTokenNotFoundException if the verificationToken is not found.
   */
  @Test
  public void testFindByUserAndTokenUserNotFound() {
    when(verificationTokenDao.findByUserAndCode(USER, TOKEN)).thenReturn(null);

    final VerificationToken actual = service.findByUserAndCode(USER, TOKEN);

    verify(verificationTokenDao, times(1)).findByUserAndCode(USER, TOKEN);
    verifyNoMoreInteractions(verificationTokenDao);

    assertNull(actual);
  }

  /**
   * Test method for {@link VerificationTokenService#findByUserAndCode(UUID, String)}.
   * 
   * @throws VerificationTokenNotFoundException if the verificationToken is not found.
   */
  @Test(expected = VerificationTokenNotFoundException.class)
  public void testFindByUserAndTokenVerificationTokenNotFoundException() {
    when(verificationTokenDao.findByUserAndCode(USER_ID, TOKEN))
        .thenThrow(new VerificationTokenNotFoundException());

    service.findByUserAndCode(USER_ID, TOKEN);
  }

  /**
   * Test method for {@link VerificationTokenService#findAll()}.
   */
  @Test
  public void testFindAll() {
    final List<VerificationToken> models = new ArrayList<>();
    when(verificationTokenDao.findAll()).thenReturn(models);

    final List<VerificationToken> actual = service.findAll();

    verify(verificationTokenDao, times(1)).findAll();
    verifyNoMoreInteractions(verificationTokenDao);

    assertThat(actual, is(models));
  }

  /**
   * Test method for
   * {@link VerificationTokenService#add(com.monogramm.starter.persistence.verificationToken.entity.VerificationToken)}.
   */
  @Test
  public void testAddVerificationToken() {
    final VerificationToken model = new VerificationToken(TOKEN, DUMMY_EXPIRY_DATE);

    service.add(model);

    ArgumentCaptor<VerificationToken> verificationTokenArgument =
        ArgumentCaptor.forClass(VerificationToken.class);
    verify(verificationTokenDao, times(1)).exists(null, TOKEN);
    verify(verificationTokenDao, times(1)).add(verificationTokenArgument.capture());
    verifyNoMoreInteractions(verificationTokenDao);

    final VerificationToken actual = verificationTokenArgument.getValue();

    assertNull(actual.getId());
    assertThat(actual.getCode(), is(model.getCode()));
    assertThat(actual.getExpiryDate(), is(model.getExpiryDate()));
  }

  /**
   * Test method for
   * {@link VerificationTokenService#add(com.monogramm.starter.persistence.verificationToken.entity.VerificationToken)}.
   */
  @Test
  public void testAddVerificationTokenAlreadyExists() {
    final VerificationToken model = new VerificationToken(TOKEN, DUMMY_EXPIRY_DATE);

    when(verificationTokenDao.exists(null, TOKEN)).thenReturn(true);

    service.add(model);

    verify(verificationTokenDao, times(1)).exists(null, TOKEN);
    verifyNoMoreInteractions(verificationTokenDao);
  }

  /**
   * Test method for
   * {@link VerificationTokenService#update(com.monogramm.starter.persistence.verificationToken.entity.VerificationToken)}.
   * 
   * @throws VerificationTokenNotFoundException if the verificationToken is not found.
   */
  @Test
  public void testUpdateVerificationToken() {
    final VerificationToken model = new VerificationToken(TOKEN, DUMMY_EXPIRY_DATE);
    model.setId(ID);

    when(verificationTokenDao.update(model)).thenReturn(model);

    final VerificationToken actual = service.update(model);

    verify(verificationTokenDao, times(1)).update(model);
    verifyNoMoreInteractions(verificationTokenDao);

    assertThat(actual, is(model));
  }

  /**
   * Test method for
   * {@link VerificationTokenService#update(com.monogramm.starter.persistence.verificationToken.entity.VerificationToken)}.
   * 
   * @throws VerificationTokenNotFoundException if the verificationToken is not found.
   */
  @Test(expected = VerificationTokenNotFoundException.class)
  public void testUpdateVerificationTokenNotFound() {
    final VerificationToken model = new VerificationToken(TOKEN, DUMMY_EXPIRY_DATE);
    model.setId(ID);

    when(verificationTokenDao.update(model)).thenReturn(null);

    service.update(model);
  }

  /**
   * Test method for
   * {@link VerificationTokenService#update(com.monogramm.starter.persistence.verificationToken.entity.VerificationToken)}.
   * 
   * @throws VerificationTokenNotFoundException if the verificationToken is not found.
   */
  @Test(expected = VerificationTokenNotFoundException.class)
  public void testUpdateVerificationTokenNotFoundException() {
    final VerificationToken model = new VerificationToken(TOKEN, DUMMY_EXPIRY_DATE);
    model.setId(ID);

    when(verificationTokenDao.update(model)).thenThrow(new VerificationTokenNotFoundException());

    service.update(model);
  }

  /**
   * Test method for {@link VerificationTokenService#deleteById(java.util.UUID)}.
   * 
   * @throws VerificationTokenNotFoundException if the verificationToken is not found.
   */
  @Test
  public void testDeleteVerificationToken() {
    when(verificationTokenDao.deleteById(ID)).thenReturn(1);

    service.deleteById(ID);

    verify(verificationTokenDao, times(1)).deleteById(ID);
    verifyNoMoreInteractions(verificationTokenDao);
  }

  /**
   * Test method for {@link VerificationTokenService#deleteById(java.util.UUID)}.
   * 
   * @throws VerificationTokenNotFoundException if the verificationToken is not found.
   */
  @Test(expected = VerificationTokenNotFoundException.class)
  public void testDeleteVerificationTokenNotFound() {
    when(verificationTokenDao.deleteById(ID)).thenReturn(null);

    service.deleteById(ID);
  }

  /**
   * Test method for {@link VerificationTokenService#deleteById(java.util.UUID)}.
   * 
   * @throws VerificationTokenNotFoundException if the verificationToken is not found.
   */
  @Test(expected = VerificationTokenNotFoundException.class)
  public void testDeleteVerificationTokenNotFoundException() {
    when(verificationTokenDao.deleteById(ID)).thenThrow(new VerificationTokenNotFoundException());

    service.deleteById(ID);
  }

}
