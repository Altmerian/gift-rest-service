package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/** Data transfer object representing a user's credentials to login*/
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetailsDTO {

  @Email
  @NotBlank(message = "Email cannot be empty")
  private String email;

  @Length(min = 6, max = 64, message = "Password length must be between 6 and 64")
  private String password;

  public UserDetailsDTO() {
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
}
