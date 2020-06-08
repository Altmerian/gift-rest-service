package com.epam.esm.controller;

import com.epam.esm.entity.Certificate;
import com.epam.esm.service.CertificateServiceImpl;
import com.epam.esm.util.RestPreconditions;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/certificates")
class CertificateRestController {

    private final CertificateServiceImpl certificateService;

    @Autowired
    public CertificateRestController(CertificateServiceImpl certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping
    public List<Certificate> getAll() {
        return certificateService.getAll();
    }

    @GetMapping("/{certificateId}")
    public Certificate getById(@PathVariable long certificateId) {
        return RestPreconditions.checkFound(certificateService.getById(certificateId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public boolean create(@RequestBody Certificate certificate) {
        Preconditions.checkNotNull(certificate);
        return certificateService.create(certificate);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable( "id" ) long id, @RequestBody Certificate certificate) {
        Preconditions.checkNotNull(certificate);
        RestPreconditions.checkFound(certificateService.getById(id));
        certificateService.update(certificate);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") long id) {
        certificateService.delete(id);
    }



}
