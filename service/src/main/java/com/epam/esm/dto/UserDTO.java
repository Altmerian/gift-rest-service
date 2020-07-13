package com.epam.esm.dto;

import com.epam.esm.entity.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

/** Data transfer object representing a user */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO extends RepresentationModel<UserDTO> {

  @JsonView(View.Public.class)
  private Long id;

  @Email(regexp = "\\w{2,40}@\\w{2,20}\\.\\w{2,4}")
  @NotBlank(message = "Email cannot be empty")
  @JsonView(View.Internal.class)
  private String email;

  @Length(min = 6, max = 64, message = "Password length must be between 6 and 64")
  private String password;

  @JsonView(View.Internal.class)
  private String firstName;

  @JsonView(View.Internal.class)
  private String lastName;

  //  @JsonView(View.Internal.class)
  private String UserRole;

  @JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
  private boolean deleted;

  @JsonIgnore private Set<Order> orders;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @JsonIgnore
  public String getPassword() {
    return password;
  }

  @JsonProperty
  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getUserRole() {
    return UserRole;
  }

  public void setUserRole(String userRole) {
    UserRole = userRole;
  }

  @JsonProperty
  public boolean isDeleted() {
    return deleted;
  }

  @JsonIgnore
  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public Set<Order> getOrders() {
    return orders;
  }

  public void setOrders(Set<Order> orders) {
    this.orders = orders;
  }
}
