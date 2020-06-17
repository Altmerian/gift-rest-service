package com.epam.esm.repository;

import com.epam.esm.entity.Certificate;
import com.epam.esm.specification.certificate.CertificateSpecification;

import java.util.List;
import java.util.Optional;

public interface CertificateRepository {

  List<Certificate> getAll();

  Optional<Certificate> get(long id);

  long create(Certificate certificate);

  void update(Certificate certificate);

  void delete(Certificate certificate);

  List<Certificate> query(CertificateSpecification specification);

  void addCertificateTag(long certificateId, long tagId);

  void clearCertificateTags(long certificateId);
}
