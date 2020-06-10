package com.epam.esm.repository;

import com.epam.esm.entity.Certificate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CertificateRepository {

  List<Certificate> getAll();

  Certificate getById(long id);

  long create(Certificate theCertificate);

  long update(Certificate theCertificate);

  boolean delete(long id);

  void addCertificateTag(long certificateId, long tagId);

  Optional<Certificate> getByNameDurationPrice(String name, int durationInDays, BigDecimal price);
}
