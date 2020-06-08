package com.epam.esm.service;

import com.epam.esm.entity.Certificate;

import java.util.List;

public interface CertificateService {

	public List<Certificate> getAll();

	public Certificate getById(long id);

	public boolean create(Certificate certificate);

	public boolean update(Certificate certificate);

	public boolean delete(long id);
	
}
