package com.epam.esm.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Represents certificate entity in the system
 */
public class Certificate {

  private Long id;
  private String name;
  private String description;
  private BigDecimal price;
  private LocalDateTime creationDate;
  private LocalDateTime modificationDate;
  private int durationInDays;
  private Set<Tag> tags;

  public Certificate() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDateTime creationDate) {
    this.creationDate = creationDate;
  }

  public LocalDateTime getModificationDate() {
    return modificationDate;
  }

  public void setModificationDate(LocalDateTime modificationDate) {
    this.modificationDate = modificationDate;
  }

  public int getDurationInDays() {
    return durationInDays;
  }

  public void setDurationInDays(int durationInDays) {
    this.durationInDays = durationInDays;
  }

  public Set<Tag> getTags() {
    return tags;
  }

  public void setTags(Set<Tag> tags) {
    this.tags = tags;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Certificate)) {
      return false;
    }

    Certificate that = (Certificate) o;

    if (!getName().equals(that.getName())) {
      return false;
    }
    if (!getPrice().equals(that.getPrice())) {
      return false;
    }
    if (getDurationInDays() != that.getDurationInDays()) {
      return false;
    }
    return getTags() != null ? getTags().equals(that.getTags()) : that.getTags() == null;
  }

  @Override
  public int hashCode() {
    int result = getName().hashCode();
    result = 31 * result + Long.hashCode(getId());
    result = 31 * result + getPrice().hashCode();
    result = 31 * result + getDurationInDays();
    result = 31 * result + (getTags() != null ? getTags().hashCode() : 0);
    return result;
  }
}
