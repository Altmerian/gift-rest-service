package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.exception.ResourceConflictException;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("api/certificates")
class CertificateRestController {

  private final CertificateService certificateService;

  @Autowired
  public CertificateRestController(CertificateService certificateService) {
    this.certificateService = certificateService;
  }

  @GetMapping
  public List<CertificateDTO> getAll(
      @RequestParam(value = "tag", defaultValue = "%") String tagName,
      @RequestParam(value = "search", defaultValue = "%") String searchFor,
      @RequestParam(value = "sort", defaultValue = "id") String sortBy) {
    return certificateService.getAll(tagName, searchFor, sortBy);
  }

  @GetMapping("/{id}")
  public CertificateDTO getById(@PathVariable long id) {
    return certificateService.getById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void create(
      @RequestBody CertificateDTO certificateDTO, HttpServletRequest req, HttpServletResponse resp)
      throws ResourceConflictException {
    if (certificateService.foundDuplicate(
        certificateDTO.getName(), certificateDTO.getDurationInDays(),
        certificateDTO.getPrice(), certificateDTO.getTags())) {
      throw new ResourceConflictException("Your data conflicts with existing resources");
    }
    long certificateId = certificateService.create(certificateDTO);
    String url = req.getRequestURL().toString();
    resp.setHeader("Location", url + certificateId);
  }

  @PutMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void update(@PathVariable("id") long id, @RequestBody CertificateDTO certificateDTO) {
    certificateService.getById(id);
    certificateService.update(id, certificateDTO);
  }

  @DeleteMapping(value = "/{id}")
  public void delete(@PathVariable("id") long id) {
    certificateService.getById(id);
    certificateService.delete(id);
  }
}
