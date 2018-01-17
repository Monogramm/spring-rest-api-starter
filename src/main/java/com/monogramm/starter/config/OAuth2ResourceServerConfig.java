/*
 * Creation by madmath03 the 2017-11-20.
 */

package com.monogramm.starter.config;

import com.monogramm.starter.api.oauth.controller.OAuthController;
import com.monogramm.starter.api.user.controller.UserController;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * OAuth2ResourceServerConfig.
 * 
 * @author madmath03
 */
@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {
  @Override
  public void configure(ResourceServerSecurityConfigurer config) {
    config.tokenServices(tokenServices());
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    /*
     * Allow all requests here. The security configuration will be done globally by the
     * OAuth2WebSecurityConfig.
     */
    http.authorizeRequests().antMatchers("/tokens/**").permitAll()
        .antMatchers(OAuthController.TOKEN_PATH, OAuthController.TOKEN_PATH + "/revokeById/**")
        .permitAll().antMatchers(UserController.REGISTER_PATH, UserController.RESET_PWD_PATH)
        .anonymous().antMatchers(UserController.VERIFY_PATH).permitAll();
  }

  @Bean
  public TokenStore tokenStore() {
    return new JwtTokenStore(accessTokenConverter());
  }

  /**
   * Access token converter.
   * 
   * @return access token converter.
   */
  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

    // TODO Use an asymmetric key: http://www.baeldung.com/spring-security-oauth-jwt#asymmetric
    converter.setSigningKey("123");

    return converter;
  }

  /**
   * Token services.
   * 
   * @return token services.
   */
  @Bean
  @Primary
  public DefaultTokenServices tokenServices() {
    final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();

    defaultTokenServices.setTokenStore(tokenStore());

    return defaultTokenServices;
  }
}
