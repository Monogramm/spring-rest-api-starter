/*
 * Creation by madmath03 the 2017-11-22.
 */

package com.monogramm.starter.config;

import com.monogramm.starter.config.component.CustomPasswordEncoder;
import com.monogramm.starter.config.filter.JsonToUrlEncodedAuthenticationFilter;
import com.monogramm.starter.config.properties.ApplicationSecurityProperties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

/**
 * Web Security Config.
 * 
 * @author madmath03
 */
@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class OAuth2WebSecurityConfig extends WebSecurityConfigurerAdapter {

  /**
   * The Spring Role prefix.
   */
  public static final String ROLE_PREFIX = "ROLE_";

  /**
   * The Spring Authorities prefix.
   */
  public static final String AUTH_PREFIX = "";

  @Autowired
  private Environment env;

  @Autowired
  private ApplicationSecurityProperties applicationSecurityProperties;

  @Autowired
  private JsonToUrlEncodedAuthenticationFilter jsonFilter;

  /**
   * Configuration of the global user details.
   * 
   * @param auth authentication manager builder. Auto wired by Spring.
   * 
   * @throws Exception if the configuration fails.
   */
  @Autowired
  public void globalUserDetails(final AuthenticationManagerBuilder auth) throws Exception {
    // TODO Add a LDAP authentication if configuration provided
    // https://spring.io/guides/gs/authenticating-ldap/

    final StringBuilder usersByUsernameQuery = new StringBuilder("select email, password, enabled ")
        .append("from user_account ").append("where email = ?");

    final StringBuilder authoritiesByUsernameQuery = new StringBuilder();
    authoritiesByUsernameQuery.append("select u.email, CONCAT('").append(ROLE_PREFIX)
        .append("', UPPER(r.name)) ").append("from user_account u, role r ")
        .append("where u.email = ? ").append("and u.role = r.id");

    final StringBuilder groupAuthoritiesByUsername = new StringBuilder();
    groupAuthoritiesByUsername.append("select r.id, r.name, CONCAT('").append(AUTH_PREFIX)
        .append("', UPPER(p.name)) ")
        .append("from role r, user_account u, permission p, role_permission rp ")
        .append("where u.email = ? ")
        .append("and p.id = rp.permission_id and r.id = rp.role_id and u.role = r.id");

    auth.jdbcAuthentication().dataSource(dataSource())
        .usersByUsernameQuery(usersByUsernameQuery.toString())
        .authoritiesByUsernameQuery(authoritiesByUsernameQuery.toString())
        .groupAuthoritiesByUsername(groupAuthoritiesByUsername.toString())
        .passwordEncoder(new CustomPasswordEncoder());
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.addFilterBefore(jsonFilter, ChannelProcessingFilter.class).csrf().and().httpBasic()
        .disable().authorizeRequests().anyRequest().authenticated().and().formLogin().permitAll();
  }

  @Override
  public void configure(final WebSecurity web) throws Exception {
    web.debug(applicationSecurityProperties.isDebug());
  }

  // JDBC configuration

  /**
   * Data source configured with the application properties.
   * 
   * @return data source.
   */
  @Bean
  public DataSource dataSource() {
    final DriverManagerDataSource dataSource = new DriverManagerDataSource();

    dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
    dataSource.setUrl(env.getProperty("spring.datasource.url"));
    dataSource.setUsername(env.getProperty("spring.datasource.username"));
    dataSource.setPassword(env.getProperty("spring.datasource.password"));

    return dataSource;
  }

}
