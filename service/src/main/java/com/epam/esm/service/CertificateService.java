package com.epam.esm.service;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.CertificatePatchDTO;
import com.epam.esm.exception.ResourceConflictException;
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
   * Gets data about  all certificates from the repository layer.
   *
   * @return list of certificates in certain transfer format
   * @param page number of page to view
   * @param size number of certificates per page
   */
  List<CertificateDTO> getAll(int page, int size);

  /**
   * Gets data of certificate with given id from the repository layer.
   *
   * @return certificate with given id in certain transfer format
   */
  CertificateDTO getById(long id);

  /**
   * Constructs {@link Specification} from given parameters and send it to appropriate repository
   * method.
   *
   * @param tagName tag name for query
   * @param searchFor part of certificate name or description for query
   * @param sortBy sorting parameters
   * @param page number of page to view
   * @param size number of certificates per page
   * @return list of certificates that matched specification in data transfer format
   */
  List<CertificateDTO> sendQuery(String tagName, String searchFor, String sortBy, int page, int size);

  /**
   * Constructs {@link Specification} from given parameters and send it to appropriate repository
   * method to count total amount of suitable certificates.
   *
   * @param tagName tag name for query
   * @param searchFor part of certificate name or description for query
   * @param sortBy sorting parameters
   * @return Overall number of certificates that matched specification
   */
  long countAll(String tagName, String searchFor, String sortBy);

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
   */
  void update(long id, CertificateDTO certificateDTO);

  /**
   * Invokes repository method to modify certificate data in the system
   *
   * @param certificatePatchDTO certificate data to apply a patch
   */
  void modify(long id, CertificatePatchDTO certificatePatchDTO);

  /**
   * Invokes repository method to delete certificate data from the system
   *
   * @param id id of the certificate to delete
   */
  void delete(long id);

  /**
   * Checks certificate data for duplicates persisted in the system.
   *
   * @param certificateDTO certificate data in a certain format for transfer
   * @throws ResourceConflictException if certificate with given name, price and duration already
   *     exists
   */
  void checkForDuplicate(CertificateDTO certificateDTO) throws ResourceConflictException;
}
