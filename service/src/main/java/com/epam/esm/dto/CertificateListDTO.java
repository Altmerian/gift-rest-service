package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Objects;

/**
 * Data transfer object representing a certificates list
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CertificateListDTO extends RepresentationModel<CertificateListDTO> {

  private List<CertificateDTO> certificates;

  public CertificateListDTO() {
  }

  public CertificateListDTO(List<CertificateDTO> certificates) {
    this.certificates = certificates;
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
    if (!(o instanceof CertificateListDTO)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    CertificateListDTO that = (CertificateListDTO) o;
    return Objects.equals(getCertificates(), that.getCertificates());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getCertificates());
  }
}
