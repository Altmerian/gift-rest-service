package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

/** Data transfer object representing a user */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonView(View.Public.class)
public class OrderDTO extends RepresentationModel<OrderDTO> {

  private Long id;

  private BigDecimal cost;

  private ZonedDateTime creationDate;

  private UserDTO user;

  @JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
  private boolean deleted;

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

  @JsonProperty
  public boolean isDeleted() {
    return deleted;
  }

  @JsonIgnore
  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public List<CertificateDTO> getCertificates() {
    return certificates;
  }

  public void setCertificates(List<CertificateDTO> certificates) {
    this.certificates = certificates;
  }
}
