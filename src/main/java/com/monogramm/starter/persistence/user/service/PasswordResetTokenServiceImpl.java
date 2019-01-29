package com.monogramm.starter.persistence.user.service;

import com.monogramm.starter.config.security.IAuthenticationFacade;
import com.monogramm.starter.dto.user.PasswordResetTokenDto;
import com.monogramm.starter.persistence.AbstractGenericService;
import com.monogramm.starter.persistence.user.dao.PasswordResetTokenRepository;
import com.monogramm.starter.persistence.user.dao.UserRepository;
import com.monogramm.starter.persistence.user.entity.PasswordResetToken;
import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.persistence.user.exception.PasswordResetTokenNotFoundException;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link PasswordResetToken} service.
 * 
 * @author madmath03
 */
@Service
public class PasswordResetTokenServiceImpl
    extends AbstractGenericService<PasswordResetToken, PasswordResetTokenDto>
    implements PasswordResetTokenService {

  /**
   * Create a {@link PasswordResetTokenServiceImpl}.
   * 
   * @param verificationTokenDao the verificationToken repository.
   * @param userDao the user repository.
   * @param authenticationFacade a facade to retrieve the authentication object.
   */
  @Autowired
  public PasswordResetTokenServiceImpl(final PasswordResetTokenRepository verificationTokenDao,
      final UserRepository userDao, IAuthenticationFacade authenticationFacade) {
    super(verificationTokenDao, userDao, new PasswordResetTokenBridge(userDao),
        authenticationFacade);
  }

  @Override
  protected PasswordResetTokenRepository getRepository() {
    return (PasswordResetTokenRepository) super.getRepository();
  }

  @Override
  public PasswordResetTokenBridge getBridge() {
    return (PasswordResetTokenBridge) super.getBridge();
  }

  @Override
  protected boolean exists(PasswordResetToken entity) {
    return getRepository().exists(entity.getId(), entity.getCode());
  }

  @Override
  protected PasswordResetTokenNotFoundException createEntityNotFoundException(
      PasswordResetToken entity) {
    return new PasswordResetTokenNotFoundException(
        "Following password reset token not found:" + entity);
  }

  @Override
  protected PasswordResetTokenNotFoundException createEntityNotFoundException(UUID entityId) {
    return new PasswordResetTokenNotFoundException("No password reset token for ID=" + entityId);
  }

  @Transactional(readOnly = true)
  @Override
  public PasswordResetToken findByUserAndCode(final User user, final String token) {
    return getRepository().findByUserAndCode(user, token);
  }

  @Transactional(readOnly = true)
  @Override
  public PasswordResetToken findByUserAndCode(final UUID userId, final String token) {
    return getRepository().findByUserAndCode(userId, token);
  }
}
