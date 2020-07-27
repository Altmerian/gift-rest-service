package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/** Data transfer object representing a user credentials */
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "UserLoginDTO")
public class UserLoginDTO {

  @Email(regexp = "\\w{2,40}@\\w{2,20}\\.\\w{2,4}")
  @NotBlank(message = "Email cannot be empty")
  private String email;

  @Length(min = 6, max = 64, message = "Password length must be between 6 and 64")
  private String password;

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
}
