package com.epam.esm.dto;

import com.epam.esm.entity.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

/** Data transfer object representing a user */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {

  private Long id;

  @Email(regexp = "\\w{2,40}@\\w{2,20}\\.\\w{2,4}")
  @NotBlank(message = "Email cannot be empty")
  private String email;

  @Length(min = 6, max = 64, message = "Password length must be between 6 and 64")
  private String password;

  private String firstName;
  private String lastName;

  private String UserRole;

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

  public String getPassword() {
    return password;
  }

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

  public Set<Order> getOrders() {
    return orders;
  }

  public void setOrders(Set<Order> orders) {
    this.orders = orders;
  }
}
