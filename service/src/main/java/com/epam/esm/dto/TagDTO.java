package com.epam.esm.dto;

import com.epam.esm.validation.NullOrNotEmptyConstraint;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/** Data transfer object representing a tag */
@JsonIgnoreProperties(ignoreUnknown = true)
@Relation(collectionRelation = "tags")
public class TagDTO extends RepresentationModel<TagDTO> {

  @Positive(message = "Tag id must be positive integer.")
  private Long id;

  @NullOrNotEmptyConstraint(message = "Tag name cannot be empty.")
  @Size(max = 64, message = "Tag name mustn't be longer than 64 characters.")
  private String name;

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
    if (!(o instanceof TagDTO)) {
      return false;
    }

    TagDTO tagDTO = (TagDTO) o;

    if (getId().equals(tagDTO.getId())) {
      return true;
    }
    return getName() != null ? getName().equals(tagDTO.getName()) : tagDTO.getName() == null;
  }

  @Override
  public int hashCode() {
    return getName() != null ? getName().hashCode() : Long.hashCode(getId());
  }
}
