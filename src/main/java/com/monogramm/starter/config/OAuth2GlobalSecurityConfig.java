/*
 * Creation by madmath03 the 2017-11-23.
 */

package com.monogramm.starter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

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
    return new OAuth2MethodSecurityExpressionHandler();
  }

}
