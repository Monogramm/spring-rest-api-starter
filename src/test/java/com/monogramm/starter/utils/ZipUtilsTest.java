/*
 * Creation by madmath03 the 2017-12-03.
 */

package com.monogramm.starter.utils;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;

import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * {@link ZipUtils} Unit Test.
 * 
 * @author madmath03
 */
public class ZipUtilsTest {

  private Path tempDirectory;
  private Path tempFile;
  private Path tempFolder;
  private Path tempHiddenFile;
  private Path zipFile;

  private Path unzipDirectory;

  /**
   * @throws java.lang.Exception If test class initialization crashes.
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {}

  /**
   * @throws java.lang.Exception If test class clean up crashes.
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {}

  /**
   * @throws java.lang.Exception If test initialization crashes.
   */
  @Before
  public void setUp() throws Exception {
    this.tempDirectory = Files.createTempDirectory("ZipUtilsTest_");
    this.tempFile = Files.createTempFile(tempDirectory, "ZipUtilsTest_File_", ".tmp");

    this.tempFolder = Files.createTempDirectory(tempDirectory, "ZipUtilsTest_Folder_");
    this.tempHiddenFile = Files.createTempFile(tempFolder, ".ZipUtilsTest_File_", ".tmp");

    this.zipFile = tempDirectory.resolve("ZipUtilsTest.zip");
    this.unzipDirectory = Files.createTempDirectory("ZipUtilsTest_Unzip_");
  }

  /**
   * @throws java.lang.Exception If test clean up crashes.
   */
  @After
  public void tearDown() throws Exception {
    Files.deleteIfExists(zipFile);
    Files.walkFileTree(this.unzipDirectory, new FileVisitor<Path>() {
      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
          throws IOException {
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Files.delete(file);
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.TERMINATE;
      }

      @Override
      public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
      }
    });
    Files.deleteIfExists(this.unzipDirectory);

    Files.delete(this.tempHiddenFile);
    Files.delete(this.tempFolder);

    Files.delete(this.tempFile);
    Files.delete(this.tempDirectory);
  }

  /**
   * Test method for {@link ZipUtils#zip(java.lang.String, java.net.URI[])}.
   * 
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testZipStringUriArray() throws IOException {
    ZipUtils.zip(zipFile.toString(), tempFile.toUri(), tempFolder.toUri());

    assertTrue(zipFile.toFile().exists());
  }

  /**
   * Test method for {@link ZipUtils#zip(java.lang.String, java.lang.String[])}.
   * 
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testZipStringStringArray() throws IOException {
    ZipUtils.zip(zipFile.toString(), tempFile.toString(), tempFolder.toString());

    assertTrue(zipFile.toFile().exists());
  }

  /**
   * Test method for {@link ZipUtils#zip(java.io.File, java.util.Collection)}.
   * 
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testZipFileCollectionOfFile() throws IOException {
    ZipUtils.zip(zipFile.toFile(), Collections.singleton(tempFile.toFile()));

    assertTrue(zipFile.toFile().exists());
  }

  /**
   * Test method for {@link ZipUtils#zip(java.io.File, java.util.Collection, boolean)}.
   * 
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testZipFileCollectionOfFileBoolean() throws IOException {
    ZipUtils.zip(zipFile.toFile(), Collections.singleton(tempFolder.toFile()), true);

    assertTrue(zipFile.toFile().exists());
  }

  /**
   * Test method for {@link ZipUtils#unzip(java.lang.String, java.net.URI[])}.
   * 
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testUnzipStringUriArray() throws IOException {
    ZipUtils.zip(zipFile.toFile(), Collections.singleton(tempFile.toFile()));

    ZipUtils.unzip(unzipDirectory.toString(), zipFile.toUri());
    assertTrue(unzipDirectory.toFile().exists());
    assertTrue(unzipDirectory.resolve(tempFile.getFileName()).toFile().exists());
  }

  /**
   * Test method for {@link ZipUtils#unzip(java.lang.String, java.lang.String[])}.
   * 
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testUnzipStringStringArray() throws IOException {
    ZipUtils.zip(zipFile.toFile(), Collections.singleton(tempFile.toFile()));

    ZipUtils.unzip(unzipDirectory.toString(), zipFile.toString());
    assertTrue(unzipDirectory.toFile().exists());
    assertTrue(unzipDirectory.resolve(tempFile.getFileName()).toFile().exists());
  }

  /**
   * Test method for {@link ZipUtils#unzip(java.io.File, java.util.Collection)}.
   * 
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testUnzipFileCollectionOfFile() throws IOException {
    ZipUtils.zip(zipFile.toFile(), Collections.singleton(tempFile.toFile()));

    ZipUtils.unzip(unzipDirectory.toFile(), Collections.singleton(zipFile.toFile()));
    assertTrue(unzipDirectory.toFile().exists());
    assertTrue(unzipDirectory.resolve(tempFile.getFileName()).toFile().exists());
  }

  /**
   * Test method for {@link ZipUtils#unzip(java.io.File, java.util.Collection, boolean)}.
   * 
   * @throws IOException if an I/O error occurs.
   */
  @Test
  public void testUnzipFileCollectionOfFileBoolean() throws IOException {
    ZipUtils.zip(zipFile.toFile(), Collections.singleton(tempFile.toFile()));

    ZipUtils.unzip(unzipDirectory.toFile(), Collections.singleton(zipFile.toFile()), true);
    assertTrue(unzipDirectory.toFile().exists());

    final String fileBaseName = FilenameUtils.getBaseName(zipFile.getFileName().toString());
    final Path subFolderPath = unzipDirectory.resolve(fileBaseName);
    assertTrue(subFolderPath.toFile().exists());
    assertTrue(subFolderPath.resolve(tempFile.getFileName()).toFile().exists());

    Files.delete(subFolderPath.resolve(tempFile.getFileName()));
    Files.delete(subFolderPath);
  }

}
