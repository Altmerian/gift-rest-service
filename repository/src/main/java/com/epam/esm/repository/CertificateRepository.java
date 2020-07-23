package com.epam.esm.repository;

import com.epam.esm.entity.Certificate;
import com.epam.esm.specification.Specification;

import java.util.List;

/** Represents base certificate repository interface for common operations with data storage. */
public interface CertificateRepository extends BaseRepository<Certificate> {

  /**
   * Makes a query for certificates that match given criteria through {@code
   * CertificateSpecification}
   *
   * @param specification certificate specification with necessary parameters
   * @param page number of page to view
   * @param size size number of certificates per page
   * @return list of certificates that match the specification
   */
  List<Certificate> query(Specification<Certificate> specification, int page, int size);

  /**
   * Counts overall quantity of certificates witch matches given specification
   *
   * @param specification certificate specification with necessary parameters
   * @return total amount of appropriate certificates
   */
  long countAll(Specification<Certificate> specification);
}
