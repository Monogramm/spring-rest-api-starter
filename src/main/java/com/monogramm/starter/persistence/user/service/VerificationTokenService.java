package com.monogramm.starter.persistence.user.service;

import com.monogramm.starter.config.security.IAuthenticationFacade;
import com.monogramm.starter.dto.user.VerificationTokenDto;
import com.monogramm.starter.persistence.AbstractGenericService;
import com.monogramm.starter.persistence.user.dao.IUserRepository;
import com.monogramm.starter.persistence.user.dao.IVerificationTokenRepository;
import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.persistence.user.entity.VerificationToken;
import com.monogramm.starter.persistence.user.exception.VerificationTokenNotFoundException;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link VerificationToken} service.
 * 
 * @author madmath03
 */
@Service
public class VerificationTokenService
    extends AbstractGenericService<VerificationToken, VerificationTokenDto>
    implements IVerificationTokenService {

  /**
   * Create a {@link VerificationTokenService}.
   * 
   * @param verificationTokenDao the verificationToken repository.
   * @param userDao the user repository.
   * @param authenticationFacade a facade to retrieve the authentication object.
   */
  @Autowired
  public VerificationTokenService(final IVerificationTokenRepository verificationTokenDao,
      final IUserRepository userDao, IAuthenticationFacade authenticationFacade) {
    super(verificationTokenDao, userDao, new VerificationTokenBridge(userDao),
        authenticationFacade);
  }

  @Override
  protected IVerificationTokenRepository getRepository() {
    return (IVerificationTokenRepository) super.getRepository();
  }

  @Override
  public VerificationTokenBridge getBridge() {
    return (VerificationTokenBridge) super.getBridge();
  }

  @Override
  protected boolean exists(VerificationToken entity) {
    return getRepository().exists(entity.getId(), entity.getCode());
  }

  @Override
  protected VerificationTokenNotFoundException createEntityNotFoundException(
      VerificationToken entity) {
    return new VerificationTokenNotFoundException(
        "Following verification token not found:" + entity);
  }

  @Override
  protected VerificationTokenNotFoundException createEntityNotFoundException(UUID entityId) {
    return new VerificationTokenNotFoundException("No verification token for ID=" + entityId);
  }

  @Transactional(readOnly = true)
  @Override
  public VerificationToken findByUserAndCode(final User user, final String token) {
    return getRepository().findByUserAndCode(user, token);
  }

  @Transactional(readOnly = true)
  @Override
  public VerificationToken findByUserAndCode(final UUID userId, final String token) {
    return getRepository().findByUserAndCode(userId, token);
  }
}
