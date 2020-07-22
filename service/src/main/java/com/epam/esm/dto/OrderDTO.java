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
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof OrderDTO)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    OrderDTO orderDTO = (OrderDTO) o;
    return isDeleted() == orderDTO.isDeleted()
        && getId().equals(orderDTO.getId())
        && Objects.equals(getCost(), orderDTO.getCost())
        && Objects.equals(getCreationDate(), orderDTO.getCreationDate())
        && Objects.equals(getUser().getId(), orderDTO.getUser().getId())
        && Objects.equals(getCertificates(), orderDTO.getCertificates());
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        super.hashCode(),
        getId(),
        getCost(),
        getCreationDate(),
        getUser().getId(),
        isDeleted(),
        getCertificates());
  }
}
