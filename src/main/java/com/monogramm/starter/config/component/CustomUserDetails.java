/*
 * Creation by madmath03 the 2018-12-23.
 */

package com.monogramm.starter.config.component;

import com.monogramm.starter.config.OAuth2WebSecurityConfig;
import com.monogramm.starter.persistence.permission.entity.Permission;
import com.monogramm.starter.persistence.role.entity.Role;
import com.monogramm.starter.persistence.user.entity.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Custom Spring Security User Details.
 * 
 * @see <a href="https://www.baeldung.com/spring-security-authentication-with-a-database">Spring
 *      Security: Authentication with a Database-backed UserDetailsService</a>
 * @see <a href="https://www.baeldung.com/spring-security-create-new-custom-security-expression">A
 *      Custom Security Expression with Spring Security</a>
 * 
 * @author madmath03
 */
public class CustomUserDetails implements UserDetails {

  /**
   * Field used for login the User Details.
   * 
   * @author madmath03
   */
  public enum LoginField {
    /**
     * User logged with its username.
     */
    USERNAME,
    /**
     * User logged with its email.
     */
    EMAIL;
  }

  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = -985006498836685422L;

  private final LoginField field;
  private final User user;

  /**
   * Create a {@link CustomUserDetails}.
   * 
   * @param field the field used for login.
   * @param user the principal user.
   */
  public CustomUserDetails(final LoginField field, final User user) {
    this.field = field;
    this.user = user;
  }

  /**
   * Returns the unique identifier used to authenticate the user.
   * 
   * @return the unique id.
   */
  public UUID getId() {
    return user.getId();
  }

  /**
   * Returns the email used to authenticate the user.
   * 
   * @return the email.
   */
  public String getEmail() {
    return user.getEmail();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.security.core.userdetails.UserDetails#getAuthorities()
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    final List<GrantedAuthority> authorities = new ArrayList<>();

    final Role role = user.getRole();
    if (role != null) {
      authorities.add(new SimpleGrantedAuthority(
          OAuth2WebSecurityConfig.ROLE_PREFIX + role.getName().toUpperCase()));

      for (final Permission permission : user.getRole().getPermissions()) {
        authorities.add(new SimpleGrantedAuthority(
            OAuth2WebSecurityConfig.AUTH_PREFIX + permission.getName().toUpperCase()));
      }
    }

    return authorities;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.security.core.userdetails.UserDetails#getPassword()
   */
  @Override
  public String getPassword() {
    return user.getPassword();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.security.core.userdetails.UserDetails#getUsername()
   */
  @Override
  public String getUsername() {
    final String username;

    switch (field) {
      case EMAIL:
        username = user.getEmail();
        break;

      default:
      case USERNAME:
        username = user.getUsername();
    }

    return username;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
   */
  @Override
  public boolean isAccountNonExpired() {
    return user.isVerified();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
   */
  @Override
  public boolean isAccountNonLocked() {
    return user.isVerified();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return user.isVerified();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.security.core.userdetails.UserDetails#isEnabled()
   */
  @Override
  public boolean isEnabled() {
    return user.isEnabled();
  }

}
