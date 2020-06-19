package com.epam.esm.service;

import com.epam.esm.dto.CertificateDTO;

import java.util.List;

public interface CertificateService {

  List<CertificateDTO> getAll();

  List<CertificateDTO> sendQuery(String tagName, String searchFor, String sortBy);

  CertificateDTO getById(long id);

  long create(CertificateDTO certificate);

  void update(long id, CertificateDTO certificate);

  void delete(long id);

  boolean foundDuplicate(CertificateDTO certificateDTO);
}
