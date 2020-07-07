package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

/** Data transfer object representing a certificate */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CertificateDTO {

  @JsonView(View.Public.class)
  private Long id;

  @JsonView(View.ExtendedPublic.class)
  @NotBlank(message = "Certificate name cannot be empty.")
  @Size(max = 64, message = "Certificate name mustn't be longer than 64 characters.")
  private String name;

  @Size(max = 128, message = "Description mustn't be longer than 128 characters.")
  private String description;

  @JsonView(View.Public.class)
  @Digits(integer = 14, fraction = 2)
  @Positive(message = "Certificate price must be positive.")
  private BigDecimal price;

  @JsonView(View.ExtendedPublic.class)
  private ZonedDateTime creationDate;

  private ZonedDateTime modificationDate;

  @JsonView(View.ExtendedPublic.class)
  @Positive(message = "Duration in days must be positive.")
  private int durationInDays;

  @Valid private Set<TagDTO> tags;

  public CertificateDTO() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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

  public ZonedDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(ZonedDateTime creationDate) {
    this.creationDate = creationDate;
  }

  public ZonedDateTime getModificationDate() {
    return modificationDate;
  }

  public void setModificationDate(ZonedDateTime modificationDate) {
    this.modificationDate = modificationDate;
  }

  public int getDurationInDays() {
    return durationInDays;
  }

  public void setDurationInDays(int durationInDays) {
    this.durationInDays = durationInDays;
  }

  public Set<TagDTO> getTags() {
    return tags;
  }

  public void setTags(Set<TagDTO> tags) {
    this.tags = tags;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof CertificateDTO)) {
      return false;
    }

    CertificateDTO that = (CertificateDTO) o;

    if (!getName().equals(that.getName())) {
      return false;
    }
    if (!getPrice().equals(that.getPrice())) {
      return false;
    }
    if (getDurationInDays() != that.getDurationInDays()) {
      return false;
    }
    return getTags() != null ? getTags().equals(that.getTags()) : that.getTags() == null;
  }

  @Override
  public int hashCode() {
    int result = getName().hashCode();
    result = 31 * result + Long.hashCode(getId());
    result = 31 * result + getPrice().hashCode();
    result = 31 * result + getDurationInDays();
    result = 31 * result + (getTags() != null ? getTags().hashCode() : 0);
    return result;
  }
}
