package com.epam.esm.service;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.TagDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface CertificateService {

  List<CertificateDTO> getAll(String tagName, String searchFor, String sortBy);

  CertificateDTO getById(long id);

  long create(CertificateDTO certificate);

  void update(long id, CertificateDTO certificate);

  void delete(long id);

  boolean foundDuplicate(String name, int durationInDays, BigDecimal price, Set<TagDTO> tags);
}
