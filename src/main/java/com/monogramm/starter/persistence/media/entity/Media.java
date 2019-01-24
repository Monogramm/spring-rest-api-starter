/*
 * Creation by madmath03 the 2019-01-24.
 */

package com.monogramm.starter.persistence.media.entity;

import com.monogramm.starter.persistence.AbstractGenericEntity;

import java.nio.file.Path;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Media.
 * 
 * @author madmath03
 */
@Entity
@Table(name = "media")
public class Media extends AbstractGenericEntity {
  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = -8067852038354449268L;

  /**
   * Display name maximum length.
   */
  public static final int MAX_LENGTH_DISPLAYNAME = 512;

  /**
   * Description maximum length.
   */
  public static final int MAX_LENGTH_DESCRIPTION = 2048;

  /**
   * Path maximum length.
   */
  public static final int MAX_LENGTH_PATH = 2048;


  /**
   * Get a new {@link MediaBuilder}.
   *
   * @return a new {@link MediaBuilder}.
   */
  public static MediaBuilder builder() {
    return new MediaBuilder();
  }

  /**
   * Get a new {@link MediaBuilder}.
   * 
   * @param name the name of your record being built.
   * @param path the path of your record being built.
   *
   * @return a new {@link MediaBuilder}.
   */
  public static MediaBuilder builder(final String name, final String path) {
    return new MediaBuilder(name, path);
  }

  /**
   * Get a new {@link MediaBuilder}.
   * 
   * @param name the name of your record being built.
   * @param path the path of your record being built.
   *
   * @return a new {@link MediaBuilder}.
   */
  public static MediaBuilder builder(final String name, final Path path) {
    return new MediaBuilder(name, path);
  }


  /**
   * The name of this record.
   */
  @Column(name = "name", unique = true, nullable = false, length = MAX_LENGTH_DISPLAYNAME)
  private String name = null;

  /**
   * The description of this record.
   */
  @Column(name = "description", nullable = true, length = MAX_LENGTH_DESCRIPTION)
  private String description = null;

  /**
   * The start date of this record.
   */
  @Column(name = "start_date", nullable = true)
  private Date startDate = null;

  /**
   * The end date of this record.
   */
  @Column(name = "end_date", nullable = true)
  private Date endDate = null;

  /**
   * The path of this record.
   */
  @Column(name = "path", nullable = false, length = MAX_LENGTH_PATH)
  private String path = null;


  /**
   * Create a {@link Media}.
   * 
   */
  public Media() {
    super();
  }

  /**
   * Create a {@link Media}.
   * 
   * @param name the name of your record to build.
   */
  public Media(String name) {
    super();

    this.name = name;
  }

  /**
   * Create a copy of a {@link Media}.
   * 
   * @param other the other entity to copy.
   * 
   * @throws NullPointerException if the {@code other} entity is @{code null}.
   */
  public Media(Media other) {
    super(other);

    this.name = other.getName();
    this.description = other.getDescription();
    this.startDate = other.getStartDate();
    this.endDate = other.getEndDate();
    this.path = other.getPath();
  }


  /**
   * Get the {@link #name}.
   * 
   * @return the {@link #name}.
   */
  public String getName() {
    return name;
  }

  /**
   * Set the {@link #name}.
   * 
   * @param name the {@link #name} to set.
   */
  public void setName(String name) {
    this.name = name;
  }


  /**
   * Get the {@link #description}.
   * 
   * @return the {@link #description}.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Set the {@link #description}.
   * 
   * @param description the {@link #description} to set.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Get the {@link #startDate}.
   * 
   * @return the {@link #startDate}.
   */
  public Date getStartDate() {
    return startDate;
  }

  /**
   * Set the {@link #startDate}.
   * 
   * @param startDate the {@link #startDate} to set.
   */
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  /**
   * Get the {@link #endDate}.
   * 
   * @return the {@link #endDate}.
   */
  public Date getEndDate() {
    return endDate;
  }

  /**
   * Set the {@link #endDate}.
   * 
   * @param endDate the {@link #endDate} to set.
   */
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  /**
   * Get the {@link #path}.
   * 
   * @return the {@link #path}.
   */
  public String getPath() {
    return path;
  }

  /**
   * Set the {@link #path}.
   * 
   * @param path the {@link #path} to set.
   */
  public void setPath(String path) {
    this.path = path;
  }

  /**
   * Set the {@link #path}.
   * 
   * @param path the {@link #path} to set.
   */
  public void setPath(Path path) {
    this.setPath(path != null ? path.toString() : null);
  }


  @Override
  public <T extends AbstractGenericEntity> void update(T entity) {
    super.update(entity);

    if (entity instanceof Media) {
      final Media media = (Media) entity;

      this.setName(media.getName());
      this.setDescription(media.getDescription());
      this.setStartDate(media.getStartDate());
      this.setEndDate(media.getEndDate());
      this.setPath(media.getPath());
    }
  }

  @Override
  public int hashCode() {
    int hash = super.hashCode();

    if (this.getName() != null) {
      hash = hash * 31 + this.getName().hashCode();
    } else {
      hash *= 31;
    }

    if (this.getDescription() == null) {
      hash *= 31;
    } else {
      hash = hash * 31 + this.getDescription().hashCode();
    }

    if (this.getStartDate() == null) {
      hash *= 31;
    } else {
      hash = hash * 31 + this.getStartDate().hashCode();
    }

    if (this.getEndDate() == null) {
      hash *= 31;
    } else {
      hash = hash * 31 + this.getEndDate().hashCode();
    }

    if (this.getPath() != null) {
      hash = hash * 31 + this.getPath().hashCode();
    } else {
      hash *= 31;
    }

    return hash;
  }

  @Override
  public boolean equals(final Object obj) {
    boolean equals = super.equals(obj);

    if (equals && obj instanceof Media) {
      final Media other = (Media) obj;
      equals = Objects.equals(getName(), other.getName())
          && Objects.equals(getDescription(), other.getDescription())
          && Objects.equals(getStartDate(), other.getStartDate())
          && Objects.equals(getEndDate(), other.getEndDate())
          && Objects.equals(getPath(), other.getPath());
    }

    return equals;
  }

  /**
   * A functional programming {@link Media} builder.
   * 
   * @author madmath03
   */
  public static class MediaBuilder extends AbstractGenericEntity.Builder<Media> {

    /**
     * Create a {@link MediaBuilder}.
     *
     */
    private MediaBuilder() {
      super();
    }

    /**
     * Create a {@link MediaBuilder}.
     * 
     * @param name the name of your record being built.
     * @param path the path of your record being built.
     */
    private MediaBuilder(final String name, final String path) {
      this();
      this.name(name);
      this.path(path);
    }

    /**
     * Create a {@link MediaBuilder}.
     * 
     * @param name the name of your record being built.
     * @param path the path of your record being built.
     */
    private MediaBuilder(final String name, final Path path) {
      this();
      this.name(name);
      this.path(path);
    }

    @Override
    protected final Media buildEntity() {
      return new Media();
    }

    /**
     * Set the name and return the builder.
     * 
     * @see Media#setName(String)
     * 
     * @param name the name of your record being built.
     * 
     * @return the builder.
     */
    public MediaBuilder name(final String name) {
      this.getEntity().setName(name);
      return this;
    }

    /**
     * Set the description and return the builder.
     * 
     * @see Media#setDescription(String)
     * 
     * @param description the description of your record being built.
     * 
     * @return the builder.
     */
    public MediaBuilder description(final String description) {
      this.getEntity().setDescription(description);
      return this;
    }

    /**
     * Set the start date and return the builder.
     * 
     * @see Media#setStartDate(Date)
     * 
     * @param startDate the start date of your record being built.
     * 
     * @return the builder.
     */
    public MediaBuilder startDate(final Date startDate) {
      this.getEntity().setStartDate(startDate);
      return this;
    }

    /**
     * Set the end date and return the builder.
     * 
     * @see Media#setEndDate(Date)
     * 
     * @param endDate the end date of your record being built.
     * 
     * @return the builder.
     */
    public MediaBuilder endDate(final Date endDate) {
      this.getEntity().setEndDate(endDate);
      return this;
    }

    /**
     * Set the path and return the builder.
     * 
     * @see Media#setPath(String)
     * 
     * @param path the path of your record being built.
     * 
     * @return the builder.
     */
    public MediaBuilder path(final String path) {
      this.getEntity().setPath(path);
      return this;
    }

    /**
     * Set the path and return the builder.
     * 
     * @see Media#setPath(Path)
     * 
     * @param path the path of your record being built.
     * 
     * @return the builder.
     */
    public MediaBuilder path(final Path path) {
      this.getEntity().setPath(path);
      return this;
    }
  }

}
