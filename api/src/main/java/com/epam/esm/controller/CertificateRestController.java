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

@RestController
@RequestMapping("api/v1/certificates")
class CertificateRestController {

  private final CertificateService certificateService;

  @Autowired
  public CertificateRestController(CertificateService certificateService) {
    this.certificateService = certificateService;
  }

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

  @GetMapping("/{id:\\d+}")
  public CertificateDTO getById(@PathVariable long id) {
    return certificateService.getById(id);
  }

  @PostMapping("/")
  @ResponseStatus(HttpStatus.CREATED)
  public void create(
      @Valid @RequestBody CertificateDTO certificateDTO,
      HttpServletRequest req,
      HttpServletResponse resp)
      throws ResourceConflictException {
    if (certificateService.foundDuplicate(certificateDTO)) {
      throw new ResourceConflictException(
          "Your data conflicts with existing resources. "
              + "Certificate with given name, price, duration and Tags already exists");
    }
    long certificateId = certificateService.create(certificateDTO);
    String url = req.getRequestURL().toString();
    resp.setHeader("Location", url + certificateId);
  }

  @PutMapping(value = "/{id:\\d+}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void update(
      @PathVariable("id") long id, @Valid @RequestBody CertificateDTO certificateDTO) {
    certificateService.update(id, certificateDTO);
  }

  @DeleteMapping(value = "/{id:\\d+}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("id") long id) {
    certificateService.delete(id);
  }
}
