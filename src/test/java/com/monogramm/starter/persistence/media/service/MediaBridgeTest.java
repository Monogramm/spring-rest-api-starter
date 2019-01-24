/*
 * Creation by madmath03 the 2019-01-24.
 */

package com.monogramm.starter.persistence.media.service;

import static org.junit.Assert.assertNull;

import com.monogramm.starter.dto.media.MediaDto;
import com.monogramm.starter.persistence.AbstractGenericBridgeTest;
import com.monogramm.starter.persistence.media.entity.Media;
import com.monogramm.starter.persistence.permission.dao.IPermissionRepository;
import com.monogramm.starter.persistence.user.dao.IUserRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link MediaBridge} Unit Test.
 * 
 * @author madmath03
 */
public class MediaBridgeTest extends AbstractGenericBridgeTest<Media, MediaDto, MediaBridge> {

  @Override
  protected MediaBridge buildTestBridge() {
    return new MediaBridge();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.monogramm.starter.persistence.AbstractGenericBridgeTest#setUp()
   */
  @Before
  public void setUp() throws Exception {
    super.setUp();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.monogramm.starter.persistence.AbstractGenericBridgeTest#tearDown()
   */
  @After
  public void tearDown() throws Exception {
    super.tearDown();
  }

  /**
   * Test method for {@link MediaBridge#MediaBridge()}.
   */
  @Test
  public void testMediaBridge() {
    final MediaBridge mediaBridge = new MediaBridge();

    super.testAbstractGenericBridge(mediaBridge);
  }

  /**
   * Test method for {@link MediaBridge#MediaBridge(IUserRepository)}.
   */
  @Test
  public void testMediaBridgeIUserRepository() {
    final MediaBridge mediaBridge = new MediaBridge(getUserRepository());

    super.testAbstractGenericBridgeIUserRepository(mediaBridge);
  }

  /**
   * Test method for {@link MediaBridge#MediaBridge(IUserRepository, IPermissionRepository)}.
   */
  @Test
  public void testMediaBridgeNull() {
    final MediaBridge mediaBridge = new MediaBridge(null);

    super.testAbstractGenericBridge(mediaBridge);
  }

  /**
   * Test method for {@link MediaBridge#buildEntity()}.
   */
  @Test
  public void testBuildEntity() {
    super.testBuildEntity();

    assertNull(this.getBridge().buildEntity().getName());
  }

  /**
   * Test method for {@link MediaBridge#buildDto()}.
   */
  @Test
  public void testBuildDto() {
    super.testBuildDto();

    assertNull(this.getBridge().buildDto().getName());
  }

}
