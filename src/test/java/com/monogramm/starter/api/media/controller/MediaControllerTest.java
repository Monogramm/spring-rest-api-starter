/*
 * Creation by madmath03 the 2019-01-26.
 */

package com.monogramm.starter.api.media.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.monogramm.starter.api.AbstractGenericController;
import com.monogramm.starter.api.AbstractGenericControllerTest;
import com.monogramm.starter.api.media.controller.MediaController;
import com.monogramm.starter.dto.media.MediaDto;
import com.monogramm.starter.persistence.AbstractGenericBridge;
import com.monogramm.starter.persistence.media.dao.MediaRepositoryIT;
import com.monogramm.starter.persistence.media.entity.Media;
import com.monogramm.starter.persistence.media.exception.MediaNotFoundException;
import com.monogramm.starter.persistence.media.service.MediaBridge;
import com.monogramm.starter.persistence.media.service.MediaService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

/**
 * {@link MediaController} Unit Test.
 * 
 * @author madmath03
 */
public class MediaControllerTest extends AbstractGenericControllerTest<Media, MediaDto> {

  /**
   * Logger for {@link MediaRepositoryIT}.
   */
  private static final Logger LOG = LoggerFactory.getLogger(MediaControllerTest.class);

  private static final String PREFIX = MediaControllerTest.class.getSimpleName() + "_";

  private static final UUID ID = UUID.randomUUID();
  private static final String DISPLAYNAME = "Foo";

  private Path tempDirectory;
  private Path tempFile;

  private HttpServletRequest httpMockRequest;
  private ServletContext mockServletContext;

  /**
   * @throws java.lang.Exception if the test setup crashes.
   */
  @Before
  public void setUp() throws Exception {
    super.setUp();

    this.httpMockRequest = mock(HttpServletRequest.class);
    assertNotNull(this.httpMockRequest);

    this.mockServletContext = mock(ServletContext.class);
    assertNotNull(this.mockServletContext);

    try {
      this.tempDirectory = Files.createTempDirectory(PREFIX);
      this.tempFile = Files.createTempFile(tempDirectory, PREFIX, ".tmp");
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  /**
   * @throws java.lang.Exception if the test cleanup crashes.
   */
  @After
  public void tearDown() throws Exception {
    Mockito.reset(getMockService());
    this.setBridge(null);

    Mockito.reset(httpMockRequest);
    Mockito.reset(mockServletContext);

    if (this.tempFile != null) {
      try {
        Files.delete(this.tempFile);
      } catch (IOException e) {
        LOG.error("Error while deleting " + tempFile, e);
      }
    }
    this.tempFile = null;

    if (this.tempDirectory != null) {
      try {
        Files.delete(this.tempDirectory);
      } catch (IOException e) {
        LOG.error("Error while deleting " + tempDirectory, e);
      }
    }
    this.tempDirectory = null;
  }

  @Override
  protected MediaController getController() {
    return (MediaController) super.getController();
  }

  @Override
  protected MediaService getMockService() {
    return (MediaService) super.getMockService();
  }

  @Override
  protected MediaService buildTestService() {
    return mock(MediaService.class);
  }

  @Override
  protected Authentication buildMockAuthentication() {
    return mock(Authentication.class);
  }

  @Override
  protected AbstractGenericController<Media, MediaDto> buildTestController() {
    return new MediaController(getMessageSource(), getEventPublisher(), getMockService());
  }

  @Override
  protected AbstractGenericBridge<Media, MediaDto> buildTestBridge() {
    return new MediaBridge();
  }

  @Override
  protected Media buildTestEntity() {
    return Media.builder(DISPLAYNAME).id(ID).build();
  }

  @Override
  protected Class<Media> getEntityClass() {
    return Media.class;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected MediaNotFoundException buildTestEntityNotFound() {
    return new MediaNotFoundException();
  }

  /**
   * Test method for {@link MediaController#MediaController(IMediaService)}.
   */
  @Test
  public void testMediaController() {
    assertNotNull(getController());
  }



  /**
   * Test method for
   * {@link MediaController#downloadMediaById(String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}.
   * 
   * @throws IOException
   * @throws FileNotFoundException
   */
  @Test
  public void testLoadMediaById() throws FileNotFoundException, IOException {
    final UUID id = UUID.randomUUID();

    final Resource resource = new PathResource(tempFile);

    when(getMockService().loadById(id)).thenReturn(resource);

    when(httpMockRequest.getServletContext()).thenReturn(mockServletContext);
    when(mockServletContext.getMimeType(resource.getFile().getAbsolutePath())).thenReturn(null);

    final ResponseEntity<Resource> actual =
        getController().downloadMediaById(id.toString(), httpMockRequest, getMockResponse());

    verify(mockServletContext, times(1)).getMimeType(resource.getFile().getAbsolutePath());
    verifyNoMoreInteractions(mockServletContext);

    verify(httpMockRequest, times(1)).getServletContext();
    verifyNoMoreInteractions(httpMockRequest);

    verify(getMockService(), times(1)).loadById(id);
    verifyNoMoreInteractions(getMockService());

    assertNotNull(actual);
    assertEquals(resource, actual.getBody());
  }

  /**
   * Test method for {@link MediaController#uploadMultipleMedia(Authentication, MultipartFile...)}.
   * 
   * @throws IOException
   * @throws FileNotFoundException
   */
  @Test
  public void testUploadMultipleMedia() throws FileNotFoundException, IOException {
    final Media model = this.buildTestEntity();
    final String fileName = tempFile.getFileName().toString();
    final byte[] fileContent = IOUtils.toByteArray(new FileInputStream(this.tempFile.toFile()));
    final MultipartFile file = new MockMultipartFile(fileName, fileContent);

    final MediaDto dto = new MediaDto();
    dto.setResource(file);

    when(getMockService().toEntity(dto)).thenReturn(null);
    when(getMockService().add(model)).thenReturn(true);

    final List<MediaDto> actualResponse =
        getController().uploadMultipleMedia(getMockAuthentication(), file);

    assertNotNull(actualResponse);
    assertTrue(actualResponse.size() == 1);
    verify(getMockService(), times(1)).toEntity(dto);
    verify(getMockService(), times(1)).add(null);
    verifyNoMoreInteractions(getMockService());
  }

}
