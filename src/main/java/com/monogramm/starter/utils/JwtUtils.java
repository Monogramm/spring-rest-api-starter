/*
 * Creation by madmath03 the 2019-01-19.
 */

package com.monogramm.starter.utils;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

/**
 * JWT Utilities.
 * 
 * @author madmath03
 */
public class JwtUtils {

  /**
   * Logger for {@link JwtUtils}.
   */
  private static final Logger LOG = LogManager.getLogger(JwtUtils.class);

  /**
   * Set the private key.
   * 
   * @param env application environment properties.
   * @param converter JWT access token converter
   * 
   * @return {@code true} if the private key has been set.
   */
  public static boolean setPrivateKey(final Environment env,
      final JwtAccessTokenConverter converter) {
    boolean keySet = false;

    final String privateKeyPath = env.getProperty("application.security.private-key-path");
    final String privateKeyPassword = env.getProperty("application.security.private-key-password");
    final String privateKeyPair = env.getProperty("application.security.private-key-pair");
    if (privateKeyPath != null && privateKeyPassword != null && privateKeyPair != null) {
      /*
       * Private key is expected to be outside of the application classpath.
       */
      final Resource resource = JwtUtils.loadResource(privateKeyPath);

      if (resource != null) {
        final KeyStoreKeyFactory keyStoreKeyFactory =
            new KeyStoreKeyFactory(resource, privateKeyPassword.toCharArray());

        converter.setKeyPair(keyStoreKeyFactory.getKeyPair(privateKeyPair));
        keySet = true;
        LOG.info("JWT verifier private key loaded.");
      } else {
        LOG.debug("Private key file was not found at path " + privateKeyPath);
      }

    }

    return keySet;
  }

  /**
   * Set the public key.
   * 
   * @param env application environment properties.
   * @param converter JWT access token converter
   * 
   * @return {@code true} if the public key has been set.
   */
  public static boolean setPublicKey(final Environment env,
      final JwtAccessTokenConverter converter) {
    boolean keySet = false;

    final String publicKeyPath = env.getProperty("application.security.public-key-path");
    if (publicKeyPath != null) {
      /*
       * Public key is expected to be outside of the application classpath.
       */
      final Resource resource = JwtUtils.loadResource(publicKeyPath);

      if (resource != null) {
        String publicKey = null;
        try {
          publicKey = IOUtils.toString(resource.getInputStream());
        } catch (final IOException e) {
          LOG.error("Public key could not be read from resource " + resource.getFilename(), e);
        }

        if (publicKey != null) {
          converter.setVerifierKey(publicKey);
          keySet = true;
          LOG.info("JWT verifier public key loaded.");
        } else {
          LOG.debug("Empty public key read from resource " + resource.getFilename());
        }
      } else {
        LOG.debug("Public key file was not found at path " + publicKeyPath);
      }

    }

    return keySet;
  }

  private static Resource loadResource(String path) {
    final Resource resource;

    final Resource externalResource = new FileSystemResource(path);
    if (externalResource.exists()) {
      resource = externalResource;
    } else {
      final Resource classPathResource = new ClassPathResource(path);

      if (classPathResource.exists()) {
        resource = externalResource;
      } else {
        resource = null;
      }
    }

    return resource;
  }

  /**
   * Set the signing key.
   * 
   * @param env application environment properties.
   * @param converter JWT access token converter
   * 
   * @return {@code true} if the signing key has been set.
   */
  public static boolean setSigningKey(final Environment env,
      final JwtAccessTokenConverter converter) {
    boolean keySet = false;

    final String signingKey = env.getProperty("application.security.signing-key", "123");
    converter.setSigningKey(signingKey);

    return keySet;
  }

  /**
   * Create a {@link JwtUtils}.
   * 
   */
  private JwtUtils() {}

}
