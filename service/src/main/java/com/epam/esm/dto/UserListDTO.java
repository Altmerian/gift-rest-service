package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

/** Data transfer object representing an users list */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserListDTO extends RepresentationModel<UserListDTO> {

  private List<UserDTO> users;

  public UserListDTO() {}

  public UserListDTO(List<UserDTO> users) {
    this.users = users;
  }

  public List<UserDTO> getUsers() {
    return users;
  }

  public void setUsers(List<UserDTO> users) {
    this.users = users;
  }
}
