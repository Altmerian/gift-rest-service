package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.exception.ResourceConflictException;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * Controller to handle all certificate related requests. Then requests depending on requests
 * parameters are handled with the appropriate method.
 */
@RestController
@RequestMapping("api/v1/certificates")
class CertificateRestController {

  /** Represents service layer to implement a domain logic and interaction with repository layer. */
  private final CertificateService certificateService;

  @Autowired
  public CertificateRestController(CertificateService certificateService) {
    this.certificateService = certificateService;
  }

  /**
   * Handles requests that aim to get data about all certificates in the system
   *
   * @param tagName request parameter for filtering by tag name
   * @param searchFor request parameter for searching by part of certificate name or certificate
   *     description
   * @param sortBy request parameter for sorting data in the response
   * @param request incoming HTTP request
   * @return response with body filled by requested data. For higher performance, information about
   *     certificate tags isn't provided. To clarify certificate tags one must use
   *     "../certificates/id" request.
   */
  @GetMapping
  public List<CertificateDTO> getAll(
      @RequestParam(value = "tag", required = false) String tagName,
      @RequestParam(value = "search", required = false) String searchFor,
      @RequestParam(value = "sort", defaultValue = "id") String sortBy,
      HttpServletRequest request) {
    if (request.getQueryString() == null || request.getQueryString().trim().isEmpty()) {
      return certificateService.getAll();
    } else {
      return certificateService.sendQuery(tagName, searchFor, sortBy);
    }
  }

  /**
   * Handles requests to get data about a certificate with a specific id
   *
   * @param id certificate id
   * @return response with payload filled by data of the searched certificate
   */
  @GetMapping("/{id:\\d+}")
  public CertificateDTO getById(@PathVariable long id) {
    return certificateService.getById(id);
  }

  /**
   * Handles requests which use POST HTTP method and request body with data of the certificate to be
   * persisted in the system
   *
   * @param certificateDTO certificate data in a certain format for transfer
   * @param req HTTP request
   * @param resp HTTP response
   * @throws ResourceConflictException if certificate with given name, price, duration and set of
   *     tags already exists
   */
  @PostMapping("/")
  @ResponseStatus(HttpStatus.CREATED)
  public void create(
      @Valid @RequestBody CertificateDTO certificateDTO,
      HttpServletRequest req,
      HttpServletResponse resp)
      throws ResourceConflictException {
    checkDuplicate(certificateDTO);
    long certificateId = certificateService.create(certificateDTO);
    String url = req.getRequestURL().toString();
    resp.setHeader("Location", url + certificateId);
  }

  /**
   * Handles requests which use PUT HTTP method and request body with data of the certificate to be
   * updated in the system
   *
   * @param id certificate id
   * @param certificateDTO certificate data in a certain format for transfer
   * @throws ResourceConflictException if certificate with given name, price, duration and set of
   *     tags already exists
   */
  @PutMapping(value = "/{id:\\d+}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void update(@PathVariable("id") long id, @Valid @RequestBody CertificateDTO certificateDTO)
      throws ResourceConflictException {
    checkDuplicate(certificateDTO);
    certificateService.update(id, certificateDTO);
  }

  /**
   * Handles requests which use DELETE HTTP method to delete all data linked with a certain
   * certificate in the system
   *
   * @param id certificate id
   */
  @DeleteMapping(value = "/{id:\\d+}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("id") long id) {
    certificateService.delete(id);
  }

  /**
   * Checks certificate data for duplicates persisted in the system.
   *
   * @param certificateDTO certificate data in a certain format for transfer
   * @throws ResourceConflictException if certificate with given name, price, duration and set of
   *     tags already exists
   */
  private void checkDuplicate(CertificateDTO certificateDTO) throws ResourceConflictException {
    if (certificateService.foundDuplicate(certificateDTO)) {
      throw new ResourceConflictException(
          "Your data conflicts with existing resources. "
              + "Certificate with given name, price, duration and Tags already exists");
    }
  }
}
