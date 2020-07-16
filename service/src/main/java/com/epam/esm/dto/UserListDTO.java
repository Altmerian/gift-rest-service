package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

/** Data transfer object representing an users list */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserListDTO extends RepresentationModel<UserListDTO> {

  private String page;
  private List<UserDTO> users;

  public UserListDTO() {}

  public UserListDTO(List<UserDTO> users) {
    this.users = users;
  }

  public String getPage() {
    return page;
  }

  public void setPage(String page) {
    this.page = page;
  }

  public List<UserDTO> getUsers() {
    return users;
  }

  public void setUsers(List<UserDTO> users) {
    this.users = users;
  }
}
