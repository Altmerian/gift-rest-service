package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

/**
 * Data transfer object representing a certificates list
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CertificateListDTO extends RepresentationModel<CertificateListDTO> {

  private String page;
  private List<CertificateDTO> certificates;

  public CertificateListDTO() {
  }

  public CertificateListDTO(List<CertificateDTO> certificates) {
    this.certificates = certificates;
  }

  public String getPage() {
    return page;
  }

  public void setPage(String page) {
    this.page = page;
  }

  public List<CertificateDTO> getCertificates() {
    return certificates;
  }

  public void setCertificates(List<CertificateDTO> certificates) {
    this.certificates = certificates;
  }
}
