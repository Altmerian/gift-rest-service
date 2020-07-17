package com.epam.esm.entity;

import com.epam.esm.audit.AuditListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/** Represents tag entity in the system */
@Entity
@EntityListeners(AuditListener.class)
@Table(name = "tags")
public class Tag {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tags_id_seq")
  @SequenceGenerator(name = "tags_id_seq", allocationSize = 1)
  private Long id;

  private String name;

  public Tag() {}

  public Tag(String name) {
    this.name = name;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Tag)) {
      return false;
    }

    Tag tag = (Tag) o;

    if (!getId().equals(tag.getId())) {
      return false;
    }
    return getName() != null ? getName().equals(tag.getName()) : tag.getName() == null;
  }

  @Override
  public int hashCode() {
    return getName() != null ? getName().hashCode() : Long.hashCode(getId());
  }
}
