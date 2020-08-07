package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UserListDTO)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    UserListDTO that = (UserListDTO) o;
    return Objects.equals(getUsers(), that.getUsers());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getUsers());
  }
}
