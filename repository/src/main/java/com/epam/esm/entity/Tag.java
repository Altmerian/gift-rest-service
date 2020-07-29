package com.epam.esm.entity;

import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/** Represents tag entity in the system */
@Entity
@Table(name = "tags")
@SequenceGenerator(name = "id_seq_gen", sequenceName = "tags_id_seq", allocationSize = 1)
@NamedNativeQuery(
    name = "getWidelyUsedTagsOfUser",
    query ="SELECT id, name FROM user_tags_function(?) WHERE tag_cost = (SELECT MAX(tag_cost) FROM user_tags_function(?))",
    resultClass = Tag.class)
public class Tag extends BaseEntity {

  private static final long serialVersionUID = -534616247517516824L;
  private String name;

  public Tag() {}

  public Tag(String name) {
    this.name = name;
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
