/*
 * Creation by madmath03 the 2019-01-25.
 */

package com.monogramm.starter.persistence.media.service;

import com.monogramm.starter.persistence.media.exception.MediaNotFoundException;
import com.monogramm.starter.persistence.media.exception.MediaStorageException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Storage Utilities.
 * 
 * @see <a href=
 *      "https://www.callicoder.com/spring-boot-file-upload-download-rest-api-example/">Spring Boot
 *      File Upload / Download Rest API Example</a>
 * 
 * @author madmath03
 */
final class StorageUtils {

  /**
   * Store a file in storage location.
   * 
   * @param file file to save.
   * @param storageLocation storage location.
   * 
   * @return path of the file stored
   * 
   * @throws MediaStorageException if file name contains invalid characters or the storage fails.
   */
  protected static Path storeFile(MultipartFile file, Path storageLocation) {
    // Normalize file name
    final String fileName = StringUtils.cleanPath(file.getOriginalFilename());

    try {
      return storeFile(fileName, file.getInputStream(), storageLocation);
    } catch (IOException e) {
      throw new MediaStorageException("Could not read file. Please try again!", e);
    }
  }

  /**
   * Store a file in storage location.
   * 
   * @param file file to save.
   * @param storageLocation storage location.
   * 
   * @return path of the file stored
   * 
   * @throws MediaStorageException if file name contains invalid characters or the storage fails.
   */
  protected static Path storeFile(String fileName, InputStream inputStream, Path storageLocation) {
    try {
      // Check if the file's name contains invalid characters
      if (fileName.contains("..")) {
        throw new MediaStorageException(
            "Sorry! Filename contains invalid path sequence " + fileName);
      }

      // Create destination folder
      Files.createDirectories(storageLocation);
      // Copy file to the target location (Replacing existing file with the same name)
      final Path targetLocation = storageLocation.resolve(fileName);
      Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);

      return targetLocation;
    } catch (IOException e) {
      throw new MediaStorageException("Could not store file " + fileName + ". Please try again!",
          e);
    }
  }

  /**
   * Load a file from storage.
   * 
   * @param filePath file path to load.
   * 
   * @return the file as resource.
   * 
   * @throws MediaNotFoundException if file is not found.
   */
  protected static Resource loadFileAsResource(Path filePath) {
    try {
      final Resource resource = new UrlResource(filePath.toUri());
      if (resource.exists()) {
        return resource;
      } else {
        throw new MediaNotFoundException("File not found " + filePath);
      }
    } catch (MalformedURLException e) {
      throw new MediaNotFoundException("Invalid file path " + filePath, e);
    }
  }

  /**
   * Delete a file from storage.
   * 
   * @param filePath file path to delete.
   * @param storageLocation storage location. *
   * @return {@code true} if file was deleted from storage location.
   * 
   * @throws MediaNotFoundException if file is not found.
   */
  protected static boolean deleteFile(final Path filePath) {
    try {
      final File file = filePath.toFile();
      if (file.isDirectory()) {
        FileUtils.cleanDirectory(filePath.toFile());
      }
      return Files.deleteIfExists(filePath);
    } catch (IOException e) {
      throw new MediaNotFoundException("File not found " + filePath, e);
    }
  }

  /**
   * Create a {@link StorageUtils}.
   * 
   */
  private StorageUtils() {}

}
