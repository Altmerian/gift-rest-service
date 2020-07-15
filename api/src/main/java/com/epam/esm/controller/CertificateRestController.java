package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.CertificateListDTO;
import com.epam.esm.dto.CertificatePatchDTO;
import com.epam.esm.dto.View;
import com.epam.esm.exception.ResourceConflictException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.util.ModelAssembler;
import com.epam.esm.util.ParseHelper;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * Controller to handle all certificate related requests. Then requests depending on requests
 * parameters are handled with the appropriate method.
 */
@RestController
@RequestMapping("/api/v1/certificates")
public class CertificateRestController {

  /** Represents service layer to implement a domain logic and interaction with repository layer. */
  private final CertificateService certificateService;

  private final ParseHelper parseHelper;

  @Autowired
  public CertificateRestController(CertificateService certificateService, ParseHelper parseHelper) {
    this.certificateService = certificateService;
    this.parseHelper = parseHelper;
  }

  /**
   * Handles requests that aim to get data about all certificates in the system
   *
   * @param tagName request parameter for filtering by tag name
   * @param searchFor request parameter for searching by part of certificate name or certificate
   *     description
   * @param sortBy request parameter for sorting data in the response
   * @return response with body filled by requested data. For higher performance, information about
   *     certificate tags isn't provided. To clarify certificate tags one must use
   *     "../certificates/id" request.
   */
  @GetMapping
  @JsonView(View.Internal.class)
  public CertificateListDTO getAll(
      @RequestParam(value = "tag", required = false) String tagName,
      @RequestParam(value = "search", required = false) String searchFor,
      @RequestParam(value = "sort", defaultValue = "id") String sortBy,
      @RequestParam(value = "page", required = false) String page,
      @RequestParam(value = "size", required = false) String size,
      HttpServletResponse resp) {
    int intPage = parseHelper.parsePage(page);
    int intSize = parseHelper.parseSize(size);
    resp.setHeader(
        "X-Total-Count", String.valueOf(certificateService.countAll(tagName, searchFor, sortBy)));
    List<CertificateDTO> certificates;
    if (StringUtils.isBlank(tagName) && StringUtils.isBlank(searchFor)) {
      certificates = certificateService.getAll(intPage, intSize);
    } else {
      certificates = certificateService.sendQuery(tagName, searchFor, sortBy, intPage, intSize);
    }
    certificates.forEach(
        certificateDTO -> ModelAssembler.addCertificateSelfLink(certificateDTO, resp));
    CertificateListDTO certificateListDTO = new CertificateListDTO(certificates);
    ModelAssembler.addCertificateListLinks(certificateListDTO);
    return certificateListDTO;
  }

  /**
   * Handles requests to get data about a certificate with a specific id
   *
   * @param id certificate id
   * @return response with payload filled by data of the searched certificate
   */
  @JsonView(View.Internal.class)
  @GetMapping("/{id:\\d+}")
  public CertificateDTO getById(@PathVariable long id, HttpServletResponse resp) {
    CertificateDTO certificateDTO = certificateService.getById(id);
    ModelAssembler.addCertificateLinks(certificateDTO, resp);
    return certificateDTO;
  }

  /**
   * Handles requests which use POST HTTP method and request body with data of the certificate to be
   * persisted in the system
   *
   * @param certificateDTO certificate data in a certain format for transfer
   * @throws ResourceConflictException if certificate with given name, price, duration and set of
   *     tags already exists
   */
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> create(@Valid @RequestBody CertificateDTO certificateDTO) {
    long certificateId = certificateService.create(certificateDTO);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(certificateId)
            .toUri();
    return ResponseEntity.created(location).build();
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
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> update(
      @PathVariable("id") long id, @Valid @RequestBody CertificateDTO certificateDTO) {
    certificateService.update(id, certificateDTO);
    return ResponseEntity.noContent().build();
  }

  /**
   * Handles requests which use the PATCH HTTP method and a request body with data to be patched in
   * the certificate. This method supports changing only for fields presenting in {@link
   * CertificatePatchDTO} class
   *
   * @param id certificate id
   * @param certificatePatchDTO certificate patching data in a certain format for transfer
   * @throws ResourceConflictException if certificate with given name, price, duration and set of
   *     tags already exists
   */
  @PatchMapping(value = "/{id:\\d+}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> patch(
      @PathVariable("id") long id, @Valid @RequestBody CertificatePatchDTO certificatePatchDTO) {
    certificateService.modify(id, certificatePatchDTO);
    return ResponseEntity.noContent().build();
  }

  /**
   * Handles requests which use DELETE HTTP method to delete the certain certificate
   *
   * @param id certificate id
   */
  @DeleteMapping(value = "/{id:\\d+}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> delete(@PathVariable("id") long id) {
    certificateService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
