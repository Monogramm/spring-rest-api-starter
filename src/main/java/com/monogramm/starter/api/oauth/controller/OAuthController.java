package com.monogramm.starter.api.oauth.controller;

import org.springframework.web.bind.annotation.RestController;

/**
 * The OAuth API Controller.
 * 
 * @author madmath03
 */
@RestController
public final class OAuthController {
  /**
   * The main data type handled by this controller.
   */
  public static final String TYPE = "oauth";
  /**
   * The request base path of this controller.
   */
  public static final String CONTROLLER_PATH = '/' + TYPE;

  /**
   * The request path for login.
   */
  public static final String TOKEN_PATH = CONTROLLER_PATH + "/token";

  /**
   * Create a {@link OAuthController}.
   * 
   * @param userService the user service.
   */
  private OAuthController() {}

}
