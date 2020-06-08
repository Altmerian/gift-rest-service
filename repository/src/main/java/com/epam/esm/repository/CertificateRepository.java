package com.epam.esm.repository;


import com.epam.esm.entity.Certificate;

import java.util.List;

public interface CertificateRepository {

    public List<Certificate> getAll();

    public Certificate getById(long id);

    public boolean create(Certificate theCertificate);

    public boolean update(Certificate theCertificate);

    public boolean delete(long id);

}
