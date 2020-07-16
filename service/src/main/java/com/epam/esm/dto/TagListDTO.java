package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

/** Data transfer object representing a tags list */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagListDTO extends RepresentationModel<TagListDTO> {

  private String page = "1 of 1";
  private List<TagDTO> tags;

  public TagListDTO() {}

  public TagListDTO(List<TagDTO> tags) {
    this.tags = tags;
  }

  public String getPage() {
    return page;
  }

  public void setPage(String page) {
    this.page = page;
  }

  public List<TagDTO> getTags() {
    return tags;
  }

  public void setTags(List<TagDTO> tags) {
    this.tags = tags;
  }
}
