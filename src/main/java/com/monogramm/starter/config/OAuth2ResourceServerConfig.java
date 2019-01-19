/*
 * Creation by madmath03 the 2017-11-20.
 */

package com.monogramm.starter.config;

import com.monogramm.starter.api.oauth.controller.OAuthController;
import com.monogramm.starter.config.OAuth2GlobalSecurityConfig.JwtConverter;
import com.monogramm.starter.utils.JwtUtils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
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

  /**
   * Logger for {@link OAuth2ResourceServerConfig}.
   */
  private static final Logger LOG = LogManager.getLogger(OAuth2ResourceServerConfig.class);

  @Autowired
  private Environment env;

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
        .antMatchers(OAuthController.TOKEN_PATH, OAuthController.REVOKE_TOKEN_PATH + "/**",
            OAuthController.REVOKE_REFRESH_TOKEN_PATH + "/**")
        .permitAll();
  }

  @Bean
  public TokenStore tokenStore() {
    return new JwtTokenStore(accessTokenConverter());
  }

  /**
   * Access token converter.
   * 
   * @see <a href="http://www.baeldung.com/spring-security-oauth-jwt">Using JWT with Spring Security
   *      OAuth</a>
   * 
   * @return access token converter.
   */
  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

    // Use an asymmetric key
    boolean asymetricKeySet = JwtUtils.setPrivateKey(env, converter);

    // XXX This should not be done if the resource server and authorization server are split
    asymetricKeySet &= JwtUtils.setPublicKey(env, converter);

    // Use symmetric key as fallback
    if (!asymetricKeySet) {
      LOG.warn("No asymetric key set. Using symmetric key for signing JWT tokens");

      JwtUtils.setSigningKey(env, converter);
    }

    converter.setAccessTokenConverter(new JwtConverter());

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
    final DefaultTokenServices tokenServices = new DefaultTokenServices();

    tokenServices.setTokenStore(tokenStore());

    return tokenServices;
  }
}
