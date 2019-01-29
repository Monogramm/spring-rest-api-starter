/*
 * Creation by madmath03 the 2019-01-24.
 */

package com.monogramm.starter.persistence.media.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * FileStorageProperties.
 * 
 * @see <a href=
 *      "https://www.callicoder.com/spring-boot-file-upload-download-rest-api-example/">Spring Boot
 *      File Upload / Download Rest API Example</a>
 * 
 * @author madmath03
 */
@ConfigurationProperties(prefix = "application.file")
public class FileStorageProperties {

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
   * Get the {@link #uploadDir}.
   * 
   * @return the {@link #uploadDir}.
   */
  public Path getUploadDir() {
    return uploadDir;
  }

  /**
   * Set the {@link #uploadDir}.
   * 
   * @param uploadDir the {@link #uploadDir} to set.
   */
  public void setUploadDir(Path uploadDir) {
    this.uploadDir = uploadDir;
  }

}
