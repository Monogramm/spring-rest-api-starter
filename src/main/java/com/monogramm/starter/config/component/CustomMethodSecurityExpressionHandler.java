package com.monogramm.starter.config.component;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

/**
 * CustomMethodSecurityExpressionHandler.
 * 
 * @see CustomMethodSecurityExpressionRoot
 * 
 * @see <a href="https://www.baeldung.com/spring-security-create-new-custom-security-expression">A
 *      Custom Security Expression with Spring Security</a>
 * 
 * @author madmath03
 */
public class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

  private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

  @Override
  protected MethodSecurityExpressionOperations createSecurityExpressionRoot(
      final Authentication authentication, final MethodInvocation invocation) {
    final CustomMethodSecurityExpressionRoot root =
        new CustomMethodSecurityExpressionRoot(authentication);

    root.setPermissionEvaluator(getPermissionEvaluator());
    root.setTrustResolver(this.trustResolver);
    root.setRoleHierarchy(getRoleHierarchy());

    return root;
  }

}
