/*
 * Creation by madmath03 the 2019-01-24.
 */

package com.monogramm.starter.persistence.media.properties;

import com.monogramm.starter.config.properties.ApplicationProperties;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * FileStorageProperties.
 * 
 * @see ApplicationProperties
 * 
 * @see <a href=
 *      "https://www.callicoder.com/spring-boot-file-upload-download-rest-api-example/">Spring Boot
 *      File Upload / Download Rest API Example</a>
 * 
 * @author madmath03
 */
@ConfigurationProperties(prefix = "application.file")
@Validated
public class FileStorageProperties {

  /**
   * The Media upload directory.
   */
  private Path uploadDir;

  /**
   * Create a {@link FileStorageProperties}.
   * 
   */
  public FileStorageProperties() {
    super();
  }

  /**
   * Create a {@link FileStorageProperties}.
   * 
   * @param uploadDir the upload directory
   */
  public FileStorageProperties(Path uploadDir) {
    super();

    this.uploadDir = uploadDir;
  }

  /**
   * Create a {@link FileStorageProperties}.
   * 
   * @param uploadDir the upload directory
   */
  public FileStorageProperties(String uploadDir) {
    this(Paths.get(uploadDir));
  }

  /**
   * Get the media upload directory.
   * 
   * @return the upload directory.
   */
  public Path getUploadDir() {
    return uploadDir;
  }

  /**
   * Set the upload directory.
   * 
   * @param uploadDir the {@link #uploadDir} to set.
   */
  public void setUploadDir(Path uploadDir) {
    this.uploadDir = uploadDir;
  }

}
