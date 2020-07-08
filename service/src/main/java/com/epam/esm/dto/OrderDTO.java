package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

/** Data transfer object representing a user */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonView(View.Public.class)
public class OrderDTO {

  private Long id;

  private BigDecimal cost;

  private ZonedDateTime creationDate;

  @JsonView(View.ExtendedPublic.class)
  private UserDTO user;

  private List<CertificateDTO> certificates;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public BigDecimal getCost() {
    return cost;
  }

  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }

  public ZonedDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(ZonedDateTime creationDate) {
    this.creationDate = creationDate;
  }

  public UserDTO getUser() {
    return user;
  }

  public void setUser(UserDTO user) {
    this.user = user;
  }

  public List<CertificateDTO> getCertificates() {
    return certificates;
  }

  public void setCertificates(List<CertificateDTO> certificates) {
    this.certificates = certificates;
  }
}
