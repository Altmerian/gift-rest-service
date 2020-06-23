package com.epam.esm.service;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.specification.Specification;

import java.util.List;

/**
 * Represents an interface of service which interacts with the underlying repository layer for
 * certificate-related actions. An instance of certificate repository {@link CertificateRepository}
 * should be aggregated during implementation.
 */
public interface CertificateService {

  /**
   * Gets data of all certificates from the repository layer.
   *
   * @return list of certificates in certain transfer format
   */
  List<CertificateDTO> getAll();

  /**
   * Gets data of certificate with given id from the repository layer.
   *
   * @return certificate with given id in certain transfer format
   */
  CertificateDTO getById(long id);

  /**
   * Constructs {@link Specification} from given parameters and send it to appropriate
   * repository method.
   *
   * @param tagName tag name for query
   * @param searchFor part of certificate name or description for query
   * @param sortBy sorting parameters
   * @return list of certificates that matched specification in data transfer format
   */
  List<CertificateDTO> sendQuery(String tagName, String searchFor, String sortBy);

  /**
   * Invokes repository method to persist certificate data in the system
   *
   * @param certificateDTO certificate data in transfer format
   * @return id of successfully persisted certificate
   */
  long create(CertificateDTO certificateDTO);

  /**
   * Invokes repository method to update certificate data in the system
   *
   * @param certificateDTO certificate data in transfer format
   * @return {@code true} if the certificate was successfully updated
   */
  void update(long id, CertificateDTO certificateDTO);

  /**
   * Invokes repository method to delete certificate data from the system
   *
   * @param id id of the certificate to delete
   * @return {@code true} if the certificate was successfully deleted
   */
  void delete(long id);

  /**
   * Finds duplicates of the given certificate in the system. Duplicate is a certificate with the
   * same name, price, duration, and tags.
   *
   * @param certificateDTO certificate data to search
   * @return {@code true} if such a duplicate was found
   */
  boolean foundDuplicate(CertificateDTO certificateDTO);
}
