/*
 * Creation by madmath03 the 2017-11-20.
 */

package com.monogramm.starter.config;

import com.monogramm.starter.config.OAuth2GlobalSecurityConfig.JwtConverter;
import com.monogramm.starter.config.component.CustomPasswordEncoder;
import com.monogramm.starter.config.component.CustomTokenEnhancer;
import com.monogramm.starter.config.properties.ApplicationSecurityProperties;
import com.monogramm.starter.persistence.user.service.UserService;
import com.monogramm.starter.utils.JwtUtils;

import java.util.Arrays;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * OAuth2AuthorizationServerConfig.
 * 
 * <p>
 * Note that, unless this is split to into a different server, this class will not used and only the
 * {@link OAuth2ResourceServerConfig} will actually loaded.
 * </p>
 * 
 * @author madmath03
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

  /**
   * Logger for {@link OAuth2AuthorizationServerConfig}.
   */
  private static final Logger LOG = LoggerFactory.getLogger(OAuth2AuthorizationServerConfig.class);

  @Autowired
  private Environment env;

  @Autowired
  private ApplicationSecurityProperties applicationSecurityProperties;

  @Autowired
  @Qualifier("authenticationManagerBean")
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserService userService;

  @Override
  public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
    oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()")
        .allowFormAuthenticationForClients();
  }

  @Override
  public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
    clients.jdbc(dataSource()).passwordEncoder(new CustomPasswordEncoder());
  }

  @Override
  public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();

    tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));

    endpoints.tokenStore(tokenStore()).accessTokenConverter(accessTokenConverter())
        .tokenEnhancer(tokenEnhancerChain).authenticationManager(authenticationManager);
  }

  /**
   * Default token services with support of refresh tokens.
   * 
   * @return default token services.
   */
  @Bean
  @Primary
  public DefaultTokenServices tokenServices() {
    final DefaultTokenServices tokenServices = new DefaultTokenServices();

    tokenServices.setTokenStore(tokenStore());
    tokenServices.setSupportRefreshToken(true);

    return tokenServices;
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
    boolean asymetricKeySet = JwtUtils.setPrivateKey(applicationSecurityProperties.getPrivateKeyPath(),
        applicationSecurityProperties.getPrivateKeyPassword(), applicationSecurityProperties.getPrivateKeyPair(),
        converter);

    // Use symmetric key as fallback
    if (!asymetricKeySet) {
      LOG.warn("No asymetric key set. Using symmetric key for signing JWT tokens");

      JwtUtils.setSigningKey(applicationSecurityProperties.getSigningKey(), converter);
    }

    converter.setAccessTokenConverter(new JwtConverter());

    return converter;
  }

  @Bean
  public TokenEnhancer tokenEnhancer() {
    return new CustomTokenEnhancer(this.userService);
  }

  @Bean
  public SessionFactory sessionFactory(HibernateEntityManagerFactory hemf) {
    return hemf.getSessionFactory();
  }

  // JDBC token store configuration

  /**
   * Data source configured with the application properties.
   * 
   * @return data source.
   */
  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();

    dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
    dataSource.setUrl(env.getProperty("spring.datasource.url"));
    dataSource.setUsername(env.getProperty("spring.datasource.username"));
    dataSource.setPassword(env.getProperty("spring.datasource.password"));

    return dataSource;
  }
}
