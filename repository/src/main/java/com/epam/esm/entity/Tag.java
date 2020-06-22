package com.epam.esm.entity;

import java.util.Set;

/**
 * Represents tag entity in the system
 */
public class Tag {
  private long id;
  private String name;
  private Set<Certificate> certificates;

  public Tag() {}

  public Tag(String name) {
    this.name = name;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<Certificate> getCertificates() {
    return certificates;
  }

  public void setCertificates(Set<Certificate> certificates) {
    this.certificates = certificates;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Tag)) return false;

    Tag tag = (Tag) o;

    if (getId() != tag.getId()) return false;
    return getName() != null ? getName().equals(tag.getName()) : tag.getName() == null;
  }

  @Override
  public int hashCode() {
    int result = (int) (getId() ^ (getId() >>> 32));
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    return result;
  }
}
