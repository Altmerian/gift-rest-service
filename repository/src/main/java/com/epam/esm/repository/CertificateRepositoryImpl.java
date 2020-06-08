package com.epam.esm.repository;

import com.epam.esm.entity.Certificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CertificateRepositoryImpl implements CertificateRepository {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public CertificateRepositoryImpl(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Certificate> getAll() {
		return null;
	}

	@Override
	public Certificate getById(long id) {
		return null;
	}

	@Override
	public boolean create(Certificate theCertificate) {
		return false;
	}

	@Override
	public boolean update(Certificate theCertificate) {
		return false;
	}

	@Override
	public boolean delete(long id) {
		return false;
	}
}











