package com.epam.esm.dto;

import com.epam.esm.validation.NullOrNotEmptyConstraint;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

/** Data transfer object representing data to apply patch to certificate */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CertificatePatchDTO {

  @NullOrNotEmptyConstraint(message = "Certificate name cannot be empty.")
  @Size(max = 64, message = "Certificate name mustn't be longer than 64 characters.")
  private String name;

  @Size(max = 128, message = "Description mustn't be longer than 128 characters.")
  private String description;

  @Digits(integer = 14, fraction = 2)
  @Positive(message = "Certificate price must be positive.")
  private BigDecimal price;

  @Positive(message = "Duration in days must be positive.")
  private int durationInDays;

  public CertificatePatchDTO() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public int getDurationInDays() {
    return durationInDays;
  }

  public void setDurationInDays(int durationInDays) {
    this.durationInDays = durationInDays;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CertificatePatchDTO)) {
      return false;
    }
    CertificatePatchDTO that = (CertificatePatchDTO) o;
    return getDurationInDays() == that.getDurationInDays()
        && Objects.equals(getName(), that.getName())
        && Objects.equals(getDescription(), that.getDescription())
        && Objects.equals(getPrice(), that.getPrice());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getDescription(), getPrice(), getDurationInDays());
  }
}
