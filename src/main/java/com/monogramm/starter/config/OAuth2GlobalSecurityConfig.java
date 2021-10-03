/*
 * Creation by madmath03 the 2017-11-23.
 */

package com.monogramm.starter.config;

import com.monogramm.starter.config.security.CustomMethodSecurityExpressionHandler;
import com.monogramm.starter.config.security.CustomPermissionEvaluator;

import java.util.Map;

import org.springframework.boot.autoconfigure.security.oauth2.resource.JwtAccessTokenConverterConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * OAuth2GlobalSecurityConfig.
 * 
 * @author madmath03
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OAuth2GlobalSecurityConfig extends GlobalMethodSecurityConfiguration {

  @Override
  protected MethodSecurityExpressionHandler createExpressionHandler() {
    final CustomMethodSecurityExpressionHandler expressionHandler =
        new CustomMethodSecurityExpressionHandler();

    // Add custom security evaluator
    expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator());

    return expressionHandler;
  }

  /**
   * JWT access token converter.
   * 
   * @see <a href=
   *      "https://stackoverflow.com/questions/46333945/accessing-a-spring-oauth-2-jwt-payload-inside-the-resource-server-controller">Accessing
   *      a Spring OAuth 2 JWT payload inside the Resource Server controller?</a>
   * 
   * @author madmath03
   */
  public static class JwtConverter extends DefaultAccessTokenConverter
      implements JwtAccessTokenConverterConfigurer {

    @Override
    public void configure(JwtAccessTokenConverter converter) {
      converter.setAccessTokenConverter(this);
    }

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
      final OAuth2Authentication auth = super.extractAuthentication(map);

      // this will get spring to copy JWT content into Authentication
      auth.setDetails(map);

      return auth;
    }
  }

}
