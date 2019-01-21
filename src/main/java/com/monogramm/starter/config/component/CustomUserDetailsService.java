/*
 * Creation by madmath03 the 2018-12-23.
 */

package com.monogramm.starter.config.component;

import com.monogramm.starter.persistence.user.dao.IUserRepository;
import com.monogramm.starter.persistence.user.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom User Details Service.
 * 
 * <strong>NOT USED YET!</strong>
 * 
 * @see <a href="https://www.baeldung.com/spring-security-authentication-with-a-database">Spring
 *      Security: Authentication with a Database-backed UserDetailsService</a>
 * @see <a href="https://www.baeldung.com/spring-security-create-new-custom-security-expression">A
 *      Custom Security Expression with Spring Security</a>
 * 
 * @author madmath03
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

  private IUserRepository userRepository;

  /**
   * Create a {@link CustomUserDetailsService}.
   * 
   * @param userRepository user repository.
   */
  @Autowired
  public CustomUserDetailsService(final IUserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(String)
   */
  @Override
  public UserDetails loadUserByUsername(String identifier) {
    final UserDetails userDetails;

    User user = userRepository.findByUsernameOrEmailIgnoreCase(identifier, identifier);
    if (user == null) {
      throw new UsernameNotFoundException(identifier);
    } else {

      // Identify which field matches the identifier
      if (identifier != null && identifier.equalsIgnoreCase(user.getEmail())) {
        userDetails = new CustomUserDetails(CustomUserDetails.LoginField.EMAIL, user);
      } else {
        userDetails = new CustomUserDetails(CustomUserDetails.LoginField.USERNAME, user);
      }

    }

    return userDetails;
  }

}
