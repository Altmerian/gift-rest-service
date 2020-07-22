package com.epam.esm.repository;

import com.epam.esm.entity.Certificate;
import com.epam.esm.specification.Specification;

import java.util.List;
import java.util.Optional;

/** Represents base certificate repository interface for common operations with data storage. */
public interface CertificateRepository {

  /**
   * Retrieves all persisted certificates
   *
   * @return list of certificates
   * @param page number of page to view
   * @param size number of certificates per page
   */
  List<Certificate> getAll(int page, int size);

  /**
   * Retrieves a certificate with given id.
   *
   * @param id certificate id
   * @return {@code Optional} containing certificate with corresponding id and other parameters or
   *     {@code Optional.empty()} if certificate with given id doesn't exist
   */
  Optional<Certificate> get(long id);

  /**
   * Persists given certificate in the repository.
   *
   * @param certificate certificate to persist
   * @return id of the successfully saved certificate
   */
  long create(Certificate certificate);

  /**
   * Updates specified certificate data stored in the repository.
   *
   * @param certificate certificate to be updated in the repository
   */
  void update(Certificate certificate);

  /**
   * Removes specified certificate from the repository.
   *
   * @param certificate certificate to be removed from repository
   */
  void delete(Certificate certificate);

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
