package com.epam.esm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

/** Represents certificate entity in the system */
@Entity
@Table(name = "certificates")
public class Certificate {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "certificates_id_seq")
  @SequenceGenerator(name = "certificates_id_seq", allocationSize = 1)
  private Long id;

  private String name;
  private String description;
  private BigDecimal price;

  @Column(
      columnDefinition = "timestamp(0) with time zone DEFAULT CURRENT_TIMESTAMP",
      insertable = false)
  private ZonedDateTime creationDate;

  private ZonedDateTime modificationDate;
  private int durationInDays;

  @Column(columnDefinition = "boolean DEFAULT false", insertable = false)
  private boolean deleted;

  @ManyToMany
  @JoinTable(
      name = "certificates_tags",
      joinColumns = @JoinColumn(name = "certificate_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id"))
  private Set<Tag> tags;

  public Certificate() {}

  @PreUpdate
  void preUpdate() {
    setModificationDate(ZonedDateTime.now());
  }

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

  public ZonedDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(ZonedDateTime creationDate) {
    this.creationDate = creationDate;
  }

  public ZonedDateTime getModificationDate() {
    return modificationDate;
  }

  public void setModificationDate(ZonedDateTime modificationDate) {
    this.modificationDate = modificationDate;
  }

  public int getDurationInDays() {
    return durationInDays;
  }

  public void setDurationInDays(int durationInDays) {
    this.durationInDays = durationInDays;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public Set<Tag> getTags() {
    return tags;
  }

  public void setTags(Set<Tag> tags) {
    this.tags = tags;
  }

  public void addTag(Tag tag) {
    this.tags.add(tag);
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
