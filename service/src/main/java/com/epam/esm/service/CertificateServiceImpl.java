package com.epam.esm.service;

import com.epam.esm.entity.Certificate;
import com.epam.esm.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateServiceImpl implements CertificateService {

	private final CertificateRepository certificateRepository;

	@Autowired
	public CertificateServiceImpl(CertificateRepository certificateRepository) {
		this.certificateRepository = certificateRepository;
	}

	@Override
	public List<Certificate> getAll() {
		return certificateRepository.getAll();
	}

	@Override
	public Certificate getById(long id) {
		return certificateRepository.getById(id);
	}

	@Override
	public boolean create(Certificate certificate) {
		return certificateRepository.create(certificate);
	}

	@Override
	public boolean update(Certificate certificate) {
		return certificateRepository.update(certificate);
	}

	@Override
	public boolean delete(long id) {
		return certificateRepository.delete(id);
	}
}





