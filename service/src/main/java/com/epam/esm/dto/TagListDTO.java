package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Objects;

/** Data transfer object representing a tags list */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagListDTO extends RepresentationModel<TagListDTO> {

  private List<TagDTO> tags;

  public TagListDTO() {}

  public TagListDTO(List<TagDTO> tags) {
    this.tags = tags;
  }

  public List<TagDTO> getTags() {
    return tags;
  }

  public void setTags(List<TagDTO> tags) {
    this.tags = tags;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TagListDTO)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    TagListDTO that = (TagListDTO) o;
    return Objects.equals(getTags(), that.getTags());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getTags());
  }
}
