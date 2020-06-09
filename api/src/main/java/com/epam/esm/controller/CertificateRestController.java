package com.epam.esm.controller;

import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.ResourceConflictException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.util.RestPreconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public List<Certificate> getAll() {
        return certificateService.getAll();
    }

    @GetMapping("/{id}")
    public Certificate getById(@PathVariable long id) {
        return certificateService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Object create(@RequestBody Certificate certificate) throws ResourceConflictException {
        if (certificateService.foundDuplicate(
                certificate.getName(), certificate.getDurationInDays(),
                certificate.getPrice(), certificate.getTags())) {
            throw new ResourceConflictException("Your data conflicts with existing resources");
        }
        long result = certificateService.create(certificate);
        return "Certificate has been created, id = " + result;
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public long update(@PathVariable("id") long id, @RequestBody Certificate certificate) {
        RestPreconditions.checkFound(certificateService.getById(id));
        return certificateService.update(certificate);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean delete(@PathVariable("id") long id) {
        return certificateService.delete(id);
    }
}
