package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TagDTO {
  private long id;
  @Size(max = 64, message
      = "Tag name mustn't be longer than 64 characters.")
  private String name;

  public TagDTO() {}

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof TagDTO)) return false;

    TagDTO tagDTO = (TagDTO) o;

    if (getId() == tagDTO.getId()) return true;
    return getName() != null ? getName().equals(tagDTO.getName()) : tagDTO.getName() == null;
  }

  @Override
  public int hashCode() {
    return getName() != null ? getName().hashCode() : Long.hashCode(getId());
  }
}
