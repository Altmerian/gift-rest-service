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
  @Email @NotBlank private String email;

  @Length(min = 6, max = 64)
  private String password;

  private String firstName;
  private String lastName;

  @JsonIgnore
  private Set<Order> orders;
}
