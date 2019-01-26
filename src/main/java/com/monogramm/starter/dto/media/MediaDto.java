/*
 * Creation by madmath03 the 2019-01-24.
 */

package com.monogramm.starter.dto.media;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.monogramm.starter.dto.AbstractGenericDto;
import com.monogramm.starter.persistence.media.entity.Media;

import java.util.Date;
import java.util.Objects;

import org.springframework.web.multipart.MultipartFile;

/**
 * MediaDto.
 * 
 * @see Media
 * 
 * @author madmath03
 */
public class MediaDto extends AbstractGenericDto {
  /**
   * The {@code serialVersionUID}.
   */
  private static final long serialVersionUID = -9024052150114444034L;


  /**
   * The name of this record.
   */
  private String name = null;

  /**
   * The description of this record.
   */
  private String description = null;

  /**
   * The start date of this record.
   */
  private Date startDate = null;

  /**
   * The end date of this record.
   */
  private Date endDate = null;

  /**
   * The path of this record.
   */
  private String path = null;

  /**
   * The resource content of this record.
   */
  private MultipartFile resource = null;


  /**
   * Create a {@link MediaDto}.
   * 
   */
  public MediaDto() {
    super();
  }

  /**
   * Create a copy of a {@link MediaDto}.
   * 
   * @param other the other DTO to copy.
   * 
   * @throws NullPointerException if the other DTO is {@code null}.
   */
  public MediaDto(MediaDto other) {
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
   * Get the {@link #resource}.
   * 
   * @return the {@link #resource}.
   */
  @JsonIgnore
  public MultipartFile getResource() {
    return resource;
  }

  /**
   * Set the {@link #resource}.
   * 
   * @param resource the {@link #resource} to set.
   */
  public void setResource(MultipartFile resource) {
    this.resource = resource;
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

    if (equals && obj instanceof MediaDto) {
      final MediaDto other = (MediaDto) obj;
      equals = Objects.equals(getName(), other.getName())
          && Objects.equals(getDescription(), other.getDescription())
          && Objects.equals(getStartDate(), other.getStartDate())
          && Objects.equals(getEndDate(), other.getEndDate())
          && Objects.equals(getPath(), other.getPath());
    }

    return equals;
  }

}
