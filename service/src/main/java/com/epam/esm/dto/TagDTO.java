package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TagDTO {
  private long id;
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

    if (getId() != tagDTO.getId()) return false;
    return getName() != null ? getName().equals(tagDTO.getName()) : tagDTO.getName() == null;
  }

  @Override
  public int hashCode() {
    int result = (int) (getId() ^ (getId() >>> 32));
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    return result;
  }
}
