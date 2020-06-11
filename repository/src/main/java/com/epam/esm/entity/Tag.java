package com.epam.esm.entity;

import java.util.Set;

public class Tag {
  private long id;
  private String name;
  private Set<Certificate> certificates;

  public Tag() {}

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

    return getName().equals(tag.getName());
  }

  @Override
  public int hashCode() {
    return getName().hashCode();
  }
}
