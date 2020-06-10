package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.ResourceConflictException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.util.RestPreconditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/certificates")
class CertificateRestController {

  private final CertificateService certificateService;
  private final ModelMapper modelMapper;

  @Autowired
  public CertificateRestController(CertificateService certificateService, ModelMapper modelMapper) {
    this.certificateService = certificateService;
    this.modelMapper = modelMapper;
  }

  @GetMapping
  public List<CertificateDTO> getAll() {
    return certificateService.getAll().stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public CertificateDTO getById(@PathVariable long id) {
    return convertToDto(certificateService.getById(id));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void create(
      @RequestBody CertificateDTO certificateDTO, HttpServletRequest req, HttpServletResponse resp)
      throws ResourceConflictException {
    Certificate certificate = convertToEntity(certificateDTO);
    if (certificateService.foundDuplicate(
        certificate.getName(), certificate.getDurationInDays(),
        certificate.getPrice(), certificate.getTags())) {
      throw new ResourceConflictException("Your data conflicts with existing resources");
    }
    long certificateId = certificateService.create(certificate);
    String url = req.getRequestURL().toString();
    resp.setHeader("Location", url + certificateId);
  }

  @PutMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public long update(@PathVariable("id") long id, @RequestBody CertificateDTO certificateDTO) {
    Certificate certificate = convertToEntity(certificateDTO);
    RestPreconditions.checkFound(certificateService.getById(id));
    return certificateService.update(certificate);
  }

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public boolean delete(@PathVariable("id") long id) {
    return certificateService.delete(id);
  }

  private CertificateDTO convertToDto(Certificate certificate) {
    return modelMapper.map(certificate, CertificateDTO.class);
  }

  private Certificate convertToEntity(CertificateDTO certificateDTO) {
    return modelMapper.map(certificateDTO, Certificate.class);
  }
}
