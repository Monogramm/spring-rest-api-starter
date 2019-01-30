/*
 * Creation by madmath03 the 2019-01-19.
 */

package com.monogramm.starter.utils;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  private static final Logger LOG = LoggerFactory.getLogger(JwtUtils.class);

  /**
   * Set the private key.
   * 
   * @param privateKeyPath private key path.
   * @param privateKeyPassword private key password.
   * @param privateKeyPair private key pair.
   * @param converter JWT access token converter
   * 
   * @return {@code true} if the private key has been set.
   */
  public static boolean setPrivateKey(final String privateKeyPath, final String privateKeyPassword,
      final String privateKeyPair, final JwtAccessTokenConverter converter) {
    boolean keySet = false;

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
        LOG.debug("Private key file was not found at path {}", privateKeyPath);
      }

    }

    return keySet;
  }

  /**
   * Set the public key.
   * 
   * @param publicKeyPath public key path.
   * @param converter JWT access token converter
   * 
   * @return {@code true} if the public key has been set.
   */
  public static boolean setPublicKey(final String publicKeyPath,
      final JwtAccessTokenConverter converter) {
    boolean keySet = false;

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
          LOG.debug("Empty public key read from resource {}", resource.getFilename());
        }
      } else {
        LOG.debug("Public key file was not found at path {}", publicKeyPath);
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
   * @param signingKey symmetric key password.
   * @param converter JWT access token converter
   * 
   * @return {@code true} if the signing key has been set.
   */
  public static boolean setSigningKey(final String signingKey,
      final JwtAccessTokenConverter converter) {
    boolean keySet = false;

    if (signingKey != null) {
      converter.setSigningKey(signingKey);
    }

    return keySet;
  }

  /**
   * Create a {@link JwtUtils}.
   * 
   */
  private JwtUtils() {}

}
