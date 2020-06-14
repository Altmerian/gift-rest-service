package com.epam.esm.repository;

import com.epam.esm.entity.Certificate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CertificateRepository {

  List<Certificate> getAll(String tagName, String searchFor, String sortBy);

  Optional<Certificate> getById(long id);

  long create(Certificate entity);

  void update(long id, Certificate entity);

  void delete(long id);

  Optional<Certificate> getByNameDurationPrice (String name, int durationInDays, BigDecimal price);

  void addCertificateTag(long certificateId, long tagId);
}
