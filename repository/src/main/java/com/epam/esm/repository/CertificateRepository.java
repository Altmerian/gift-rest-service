package com.epam.esm.repository;

import com.epam.esm.entity.Certificate;
import com.epam.esm.specification.Specification;

import java.util.List;
import java.util.Optional;

/**
 * Represents base certificate repository interface for common operations with data storage.
 */
public interface CertificateRepository {

  /**
   * Retrieves all persisted certificates
   *
   * @return list of certificates
   */
  List<Certificate> getAll();

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
   * Makes a query for certificates that match given criteria through {@code CertificateSpecification}
   *
   * @param specification certificate specification with necessary parameters
   * @return list of certificates that match the specification
   */
  List<Certificate> query(Specification<Certificate> specification);

  /**
   * Saves data about certificate tag in the repository
   *
   * @param certificateId certificate id
   * @param tagId tag id
   */
  void addCertificateTag(long certificateId, long tagId);

  /**
   * Purges data in repository about certificate tags
   *
   * @param certificateId certificate id
   */
  void clearCertificateTags(long certificateId);
}
