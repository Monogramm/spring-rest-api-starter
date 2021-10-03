/*
 * Creation by madmath03 the 2019-01-24.
 */

package com.monogramm.starter.persistence.media.dao;

import com.monogramm.starter.persistence.GenericRepository;
import com.monogramm.starter.persistence.media.entity.Media;

import java.util.List;
import java.util.UUID;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * The {@link Media} Data Access Object (DAO) interface.
 * 
 * @author madmath03
 */
@Repository
public interface MediaRepository extends GenericRepository<Media> {

  /**
   * Find all Medias from the repository containing the name while ignoring case.
   * 
   * @param name the name content to search.
   * 
   * @return the list of all the Medias matching the search through the repository.
   */
  @Query("FROM Media AS m " + "WHERE LOWER(m.name) LIKE concat('%', LOWER(:name), '%')")
  List<Media> findAllContainingNameIgnoreCase(@Param("name") final String name);

  /**
   * Find an Media through its name while ignoring case.
   * 
   * @param name the name to search.
   * 
   * @return the Media matching the name.
   * 
   * @throws NoResultException if no Media matches the name in the repository.
   * @throws NonUniqueResultException if several Medias match the name in the repository.
   */
  Media findByNameIgnoreCase(final String name);

  /**
   * Tests if an entity exists in the repository for the given primary key or the name.
   * 
   * @param mediaId the identifier of the Media to check existence.
   * @param name the name of the Media to check existence.
   * 
   * @return {@code true} if Media exists, {@code false} otherwise.
   */
  @Query("SELECT count(m) > 0 FROM Media AS m "
      + "WHERE m.id = :MediaId OR LOWER(m.name) = LOWER(:name)")
  boolean exists(@Param("MediaId") final UUID mediaId, @Param("name") final String name);

}
