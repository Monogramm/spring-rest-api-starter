package com.monogramm.starter.persistence.user.service;

import com.monogramm.starter.dto.user.RegistrationDto;
import com.monogramm.starter.dto.user.UserDto;
import com.monogramm.starter.persistence.AbstractGenericService;
import com.monogramm.starter.persistence.role.dao.IRoleRepository;
import com.monogramm.starter.persistence.role.entity.Role;
import com.monogramm.starter.persistence.role.exception.RoleNotFoundException;
import com.monogramm.starter.persistence.user.dao.IUserRepository;
import com.monogramm.starter.persistence.user.entity.User;
import com.monogramm.starter.persistence.user.exception.UserNotFoundException;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService extends AbstractGenericService<User, UserDto> implements IUserService {

  /**
   * Logger for {@link UserService}.
   */
  private static final Logger LOG = LogManager.getLogger(UserService.class);

  public static final String DEFAULT_ROLE = "User";

  private final IRoleRepository roleRepository;

  /**
   * Create a {@link UserService}.
   * 
   * @param userDao the user repository.
   * @param roleDao the role repository.
   * 
   * @throws IllegalArgumentException if {@code roleDao} is {@code null}.
   */
  @Autowired
  public UserService(final IUserRepository userDao, final IRoleRepository roleDao) {
    super(userDao, userDao, new UserBridge(userDao, roleDao));
    if (roleDao == null) {
      throw new IllegalArgumentException("Role repository cannot be null.");
    }
    this.roleRepository = roleDao;
  }

  /**
   * Get the {@link #roleRepository}.
   * 
   * @return the {@link #roleRepository}.
   */
  protected final IRoleRepository getRoleRepository() {
    return roleRepository;
  }

  @Override
  protected IUserRepository getRepository() {
    return (IUserRepository) super.getRepository();
  }

  @Override
  public UserBridge getBridge() {
    return (UserBridge) super.getBridge();
  }

  @Override
  protected boolean exists(User entity) {
    return getRepository().exists(entity.getId(), entity.getUsername(), entity.getEmail());
  }

  @Override
  protected UserNotFoundException createEntityNotFoundException(User entity) {
    return new UserNotFoundException("Following user not found:" + entity);
  }

  @Override
  protected UserNotFoundException createEntityNotFoundException(UUID entityId) {
    return new UserNotFoundException("No user for ID=" + entityId);
  }

  @Transactional(readOnly = true)
  @Override
  public List<User> findAllContainingUsername(final String username) {
    return getRepository().findAllContainingUsernameIgnoreCase(username);
  }

  @Transactional(readOnly = true)
  @Override
  public List<User> findAllContainingEmail(final String email) {
    return getRepository().findAllContainingEmailIgnoreCase(email);
  }

  @Transactional(readOnly = true)
  @Override
  public List<User> findAllContainingUsernameOrEmail(final String username, final String email) {
    return getRepository().findAllContainingUsernameOrEmailIgnoreCase(username, email);
  }

  @Transactional(readOnly = true)
  @Override
  public User findByUsername(String username) {
    return getRepository().findByUsernameIgnoreCase(username);
  }

  @Transactional(readOnly = true)
  @Override
  public User findByEmail(String email) {
    return getRepository().findByEmailIgnoreCase(email);
  }

  @Transactional(readOnly = true)
  @Override
  public User findByUsernameOrEmail(String username, String email) {
    User user;
    try {
      user = getRepository().findByUsernameOrEmailIgnoreCase(username, email);
    } catch (UserNotFoundException e) {
      LOG.debug(e);
      user = null;
    }
    return user;
  }

  @Override
  public User setPassword(final UUID userId, char[] password) {
    final User updatedEntity = getRepository().setPassword(userId, password);

    if (updatedEntity == null) {
      throw this.createEntityNotFoundException(userId);
    }

    return updatedEntity;
  }

  @Override
  public User setEnabled(final UUID userId, boolean enabled) {
    final User updatedEntity = getRepository().setEnabled(userId, enabled);

    if (updatedEntity == null) {
      throw this.createEntityNotFoundException(userId);
    }

    return updatedEntity;
  }

  @Override
  public User setVerified(final UUID userId, boolean verified) {
    final User updatedEntity = getRepository().setVerified(userId, true);

    if (updatedEntity == null) {
      throw this.createEntityNotFoundException(userId);
    }

    return updatedEntity;
  }

  @Override
  public boolean register(final RegistrationDto registration) {
    final User user = User.builder(registration.getUsername(), registration.getEmail())
        .password(registration.getPassword()).build();

    /*
     * TODO Add password strengths and rules.
     * http://www.baeldung.com/registration-password-strength-and-rules
     */
    final Role defaultRole;
    try {
      defaultRole = roleRepository.findByNameIgnoreCase(DEFAULT_ROLE);
    } catch (RoleNotFoundException e) {
      LOG.error(e);
      throw e;
    }
    user.setRole(defaultRole);

    return this.add(user);
  }
}
