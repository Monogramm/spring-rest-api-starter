/*
 * Creation by madmath03 the 2017-11-29.
 */

package com.monogramm.starter.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FilenameUtils;

/**
 * ZipUtils.
 * 
 * @author madmath03
 */
public final class ZipUtils {

  /**
   * Archive file(s) to a destination ZIP archive.
   * 
   * <p>
   * Hidden files will not be treated and archived.
   * </p>
   * 
   * @param destination the destination path for the ZIP file.
   * @param sources the files/folders to archive.
   * 
   * @throws FileNotFoundException if the file exists but is a directory rather than a regular file,
   *         does not exist but cannot be created, or cannot be opened for any other reason
   * 
   * @throws IOException if an I/O error occurs.
   * 
   * @throws ZipException if a ZIP file error has occurred.
   * 
   * @throws SecurityException if a security manager exists and its {@code checkWrite} method denies
   *         write access to the file.
   * 
   */
  public static void zip(final String destination, final URI... sources) throws IOException {
    final File destFile = Paths.get(destination).toFile();
    final Collection<File> srcFiles;

    if (sources == null || sources.length == 0) {
      srcFiles = Collections.emptyList();
    } else {
      srcFiles = new ArrayList<>(sources.length);
      for (final URI src : sources) {
        srcFiles.add(Paths.get(src).toFile());
      }
    }

    ZipUtils.zip(destFile, srcFiles);
  }

  /**
   * Archive file(s) to a destination ZIP archive.
   * 
   * <p>
   * Hidden files will not be treated and archived.
   * </p>
   * 
   * @param destination the destination path for the ZIP file.
   * @param sources the files/folders to archive.
   * 
   * @throws FileNotFoundException if the file exists but is a directory rather than a regular file,
   *         does not exist but cannot be created, or cannot be opened for any other reason
   * 
   * @throws IOException if an I/O error occurs.
   * 
   * @throws ZipException if a ZIP file error has occurred.
   * 
   * @throws SecurityException if a security manager exists and its {@code checkWrite} method denies
   *         write access to the file.
   * 
   */
  public static void zip(final String destination, final String... sources) throws IOException {
    final File destFile = Paths.get(destination).toFile();
    final Collection<File> srcFiles;

    if (sources == null || sources.length == 0) {
      srcFiles = Collections.emptyList();
    } else {
      srcFiles = new ArrayList<>(sources.length);
      for (final String src : sources) {
        srcFiles.add(Paths.get(src).toFile());
      }
    }

    ZipUtils.zip(destFile, srcFiles);
  }

  /**
   * Archive file(s) to a destination ZIP archive.
   * 
   * <p>
   * Hidden files will not be treated and archived.
   * </p>
   * 
   * @param destination the destination path for the ZIP file.
   * @param sources the files/folders to archive.
   * 
   * @throws FileNotFoundException if the file exists but is a directory rather than a regular file,
   *         does not exist but cannot be created, or cannot be opened for any other reason
   * 
   * @throws IOException if an I/O error occurs.
   * 
   * @throws ZipException if a ZIP file error has occurred.
   * 
   * @throws SecurityException if a security manager exists and its {@code checkWrite} method denies
   *         write access to the file.
   * 
   */
  public static void zip(final File destination, final Collection<File> sources)
      throws IOException {
    zip(destination, sources, false);
  }

  /**
   * Archive file(s) to a destination ZIP archive.
   * 
   * @param destination the destination path for the ZIP file.
   * @param sources the files/folders to archive.
   * @param forceHidden either hidden files/folders should be treated in the ZIP archive.
   * 
   * @throws FileNotFoundException if the file exists but is a directory rather than a regular file,
   *         does not exist but cannot be created, or cannot be opened for any other reason
   * 
   * @throws IOException if an I/O error occurs.
   * 
   * @throws ZipException if a ZIP file error has occurred.
   * 
   * @throws SecurityException if a security manager exists and its {@code checkWrite} method denies
   *         write access to the file.
   * 
   */
  public static void zip(final File destination, final Collection<File> sources,
      final boolean forceHidden) throws IOException {

    // Open output stream for ZIP file
    try (FileOutputStream fos = new FileOutputStream(destination);
        ZipOutputStream zipOut = new ZipOutputStream(fos)) {

      // For each source file
      for (final File srcFile : sources) {

        if (srcFile.isHidden() && !forceHidden) {
          continue;
        } else {
          zipFilesAndFolder(zipOut, srcFile, srcFile.getName());
        }

      }

    }

  }

  private static void zipFilesAndFolder(final ZipOutputStream zipOut, final File source,
      final String fileZipPath) throws IOException {
    if (source.isDirectory()) {
      final File[] children = source.listFiles();

      for (File childFile : children) {
        zipFilesAndFolder(zipOut, childFile, fileZipPath + "/" + source.getName());
      }

    } else {
      zipFile(zipOut, source, source.getName());
    }
  }

  private static void zipFile(final ZipOutputStream zipOut, final File source,
      final String fileZipPath) throws IOException {
    byte[] bytes = new byte[1024];

    // Open input stream for source file
    try (FileInputStream fis = new FileInputStream(source)) {
      ZipEntry zipEntry = new ZipEntry(fileZipPath);
      zipOut.putNextEntry(zipEntry);

      // Write content of file to the ZIP output stream
      int length;
      while ((length = fis.read(bytes)) >= 0) {
        zipOut.write(bytes, 0, length);
      }
    }

  }

  /**
   * Unzip ZIP file(s) to a destination folder.
   * 
   * <p>
   * Sub folders named after the source ZIP file(s) will be created in the destination folder if
   * there is at least 2 ZIP files to unzip.
   * </p>
   * 
   * @param destination the destination path for unzipping the ZIP file(s).
   * @param sources the ZIP file(s).
   * 
   * @throws FileNotFoundException if any of the source ZIP files does not exist, is a directory
   *         rather than a regular file, or for some other reason cannot be opened for reading.
   * 
   * @throws IOException if an I/O error occurs.
   * 
   * @throws ZipException if a ZIP file error has occurred
   * 
   * @throws SecurityException if a security manager exists and its {@code checkRead} method denies
   *         read access to any of the ZIP file.
   * 
   */
  public static void unzip(final String destination, final URI... sources) throws IOException {
    final File destFile = Paths.get(destination).toFile();
    final Collection<File> srcFiles;

    if (sources == null || sources.length == 0) {
      srcFiles = Collections.emptyList();
    } else {
      srcFiles = new ArrayList<>(sources.length);
      for (final URI src : sources) {
        srcFiles.add(Paths.get(src).toFile());
      }
    }

    ZipUtils.unzip(destFile, srcFiles);
  }

  /**
   * Unzip ZIP file(s) to a destination folder.
   * 
   * <p>
   * Sub folders named after the source ZIP file(s) will be created in the destination folder if
   * there is at least 2 ZIP files to unzip.
   * </p>
   * 
   * @param destination the destination path for unzipping the ZIP file(s).
   * @param sources the ZIP file(s).
   * 
   * @throws FileNotFoundException if any of the source ZIP files does not exist, is a directory
   *         rather than a regular file, or for some other reason cannot be opened for reading.
   * 
   * @throws IOException if an I/O error occurs.
   * 
   * @throws ZipException if a ZIP file error has occurred
   * 
   * @throws SecurityException if a security manager exists and its {@code checkRead} method denies
   *         read access to any of the ZIP file.
   * 
   */
  public static void unzip(final String destination, final String... sources) throws IOException {
    final File destFile = Paths.get(destination).toFile();
    final Collection<File> srcFiles;

    if (sources == null || sources.length == 0) {
      srcFiles = Collections.emptyList();
    } else {
      srcFiles = new ArrayList<>(sources.length);
      for (final String src : sources) {
        srcFiles.add(Paths.get(src).toFile());
      }
    }

    ZipUtils.unzip(destFile, srcFiles);
  }

  /**
   * Unzip ZIP file(s) to a destination folder.
   * 
   * <p>
   * Sub folders named after the source ZIP file(s) will be created in the destination folder if
   * there is at least 2 ZIP files to unzip.
   * </p>
   * 
   * @param destination the destination path for unzipping the ZIP file(s).
   * @param sources the ZIP file(s).
   * 
   * @throws FileNotFoundException if any of the source ZIP files does not exist, is a directory
   *         rather than a regular file, or for some other reason cannot be opened for reading.
   * 
   * @throws IOException if an I/O error occurs.
   * 
   * @throws ZipException if a ZIP file error has occurred
   * 
   * @throws SecurityException if a security manager exists and its {@code checkRead} method denies
   *         read access to any of the ZIP file.
   * 
   */
  public static void unzip(final File destination, final Collection<File> sources)
      throws IOException {
    unzip(destination, sources, sources.size() > 1);
  }

  /**
   * Unzip ZIP file(s) to a destination folder.
   * 
   * @param destination the destination path for unzipping the ZIP file(s).
   * @param sources the ZIP file(s).
   * @param subFolder either sub folders named after the source ZIP file should be created in the
   *        destination folder.
   * 
   * @throws FileNotFoundException if any of the source ZIP files does not exist, is a directory
   *         rather than a regular file, or for some other reason cannot be opened for reading.
   * 
   * @throws IOException if an I/O error occurs.
   * 
   * @throws ZipException if a ZIP file error has occurred.
   * 
   * @throws SecurityException if a security manager exists and its {@code checkRead} method denies
   *         read access to any of the ZIP file.
   * 
   */
  public static void unzip(final File destination, final Collection<File> sources,
      final boolean subFolder) throws IOException {

    // For each source ZIP file
    for (final File srcFile : sources) {

      // Open output stream for ZIP file
      try (FileInputStream fis = new FileInputStream(srcFile);
          ZipInputStream zipIn = new ZipInputStream(fis)) {
        final String output;
        if (subFolder) {
          final Path subFolderPath = Paths.get(destination.getAbsolutePath(),
              FilenameUtils.getBaseName(srcFile.getName()));

          if (!subFolderPath.toFile().exists()) {
            Files.createDirectory(subFolderPath);
          }

          output = subFolderPath.toString();
        } else {
          output = destination.getAbsolutePath();
        }

        unzipFiles(zipIn, output);

      }

    }

  }

  private static void unzipFiles(final ZipInputStream zipIn, final String fileOutputPath)
      throws IOException {
    byte[] bytes = new byte[1024];

    ZipEntry zipEntry = zipIn.getNextEntry();

    // For each ZIP entry in the source
    for (; zipEntry != null; zipEntry = zipIn.getNextEntry()) {
      final String fileName = zipEntry.getName();
      final File newFile = new File(fileOutputPath + '/' + fileName);

      // Open output stream for ZIP entry
      try (FileOutputStream fos = new FileOutputStream(newFile)) {

        // Write content of ZIP entry to the file output stream
        int length;
        while ((length = zipIn.read(bytes)) > 0) {
          fos.write(bytes, 0, length);
        }
      }

    }

  }

  /**
   * Hidden constructor.
   */
  private ZipUtils() {}
}
