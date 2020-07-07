package com.epam.esm.dto;

import com.epam.esm.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

  @JsonIgnore
  private User user;

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

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<CertificateDTO> getCertificates() {
    return certificates;
  }

  public void setCertificates(List<CertificateDTO> certificates) {
    this.certificates = certificates;
  }
}
