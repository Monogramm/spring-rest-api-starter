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
   * Set the private key.
   * 
   * @param privateKeyPath private key path.
   * @param privateKeyPassword private key password.
   * @param privateKeyPair private key pair.
   * @param converter JWT access token converter
   * 
   * @return {@code true} if the private key has been set.
   */
  public static boolean setKeyPair(final String privateKeyPath, final String privateKeyPassword,
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
        LOG.info("JWT Key Pair loaded.");
      } else {
        LOG.debug("Key Pair file was not found at path {}", privateKeyPath);
      }

    }

    return keySet;
  }

  /**
   * Set the verifier key from a resource.
   * 
   * @param verifierKeyPath verifier key path.
   * @param converter JWT access token converter
   * 
   * @return {@code true} if the public key has been set.
   */
  public static boolean setVerifierKeyFromPath(final String verifierKeyPath,
      final JwtAccessTokenConverter converter) {
    boolean keySet = false;

    if (verifierKeyPath != null) {
      /*
       * Public key is expected to be outside of the application classpath.
       */
      final Resource resource = JwtUtils.loadResource(verifierKeyPath);

      if (resource != null) {
        String verifierKeyKey = null;
        try {
          verifierKeyKey = IOUtils.toString(resource.getInputStream());
        } catch (final IOException e) {
          LOG.error("Verifier key could not be read from resource " + resource.getFilename(), e);
        }

        keySet = setVerifierKey(verifierKeyKey, converter);
      } else {
        LOG.debug("Verifier key file was not found at path {}", verifierKeyPath);
      }

    }

    return keySet;
  }

  /**
   * Set the verifier key.
   * 
   * @param verifierKey verifier key.
   * @param converter JWT access token converter
   * 
   * @return {@code true} if the public key has been set.
   */
  public static boolean setVerifierKey(final String verifierKey,
      final JwtAccessTokenConverter converter) {
    boolean keySet = false;

    if (verifierKey != null) {
      converter.setVerifierKey(verifierKey);
      keySet = true;
      LOG.info("JWT verifier public key loaded.");
    } else {
      LOG.debug("Empty verifier key!");
    }

    return keySet;
  }

  /**
   * Set the signing key.
   * 
   * @param signingKeyPath symmetric key password.
   * @param converter JWT access token converter
   * 
   * @return {@code true} if the signing key has been set.
   */
  public static boolean setSigningKeyFromPath(final String signingKeyPath,
      final JwtAccessTokenConverter converter) {
    boolean keySet = false;

    if (signingKeyPath != null) {
      /*
       * Signing key is expected to be outside of the application classpath.
       */
      final Resource resource = JwtUtils.loadResource(signingKeyPath);

      if (resource != null) {
        String signingKey = null;
        try {
          signingKey = IOUtils.toString(resource.getInputStream());
        } catch (final IOException e) {
          LOG.error("Signing key could not be read from resource " + resource.getFilename(), e);
        }

        keySet = setSigningKey(signingKey, converter);
      } else {
        LOG.debug("Signing key file was not found at path {}", signingKeyPath);
      }

    }

    return keySet;
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
      keySet = true;
      LOG.info("JWT signing private key loaded.");
    }

    return keySet;
  }

  /**
   * Create a {@link JwtUtils}.
   * 
   */
  private JwtUtils() {}

}
