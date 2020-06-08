package com.epam.esm.repository;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Repository
public class CertificateRepositoryImpl implements CertificateRepository {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public CertificateRepositoryImpl(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Certificate> getAll() {
		String SQL_GET_ALL = "SELECT * FROM certificates";
		return jdbcTemplate.query(SQL_GET_ALL, new CertificateMapper());
	}

	@Override
	public Certificate getById(long id) {
		String SQL_GET_BY_ID = "SELECT * FROM certificates WHERE id = ?";
		return jdbcTemplate.queryForObject(SQL_GET_BY_ID, new Object[] { id }, new CertificateMapper());
	}

	@Override
	public boolean create(Certificate certificate) {
		long certificateId = saveCertificate(certificate);
		return false;
	}

	private long saveCertificate(Certificate certificate) {
		String SQL_INSERT = "INSERT INTO certificates " +
			"(owner_name, description, price, creation_date, expiration_date) " +
			"VALUES (?, ?, ?, ?, ?)";
		PreparedStatementCreator psc = new PreparedStatementCreatorFactory(
				SQL_INSERT, Types.VARCHAR, Types.VARCHAR, Types.NUMERIC,
				Types.TIMESTAMP_WITH_TIMEZONE, Types.DATE).newPreparedStatementCreator(
				Arrays.asList(
						certificate.getOwnerName(),
						certificate.getDescription(),
						certificate.getPrice(),
						Timestamp.valueOf(certificate.getCreationDate()),
						Date.valueOf(certificate.getExpirationDate())
				)
		);

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, keyHolder);

		return  Objects.requireNonNull(keyHolder.getKey()).longValue();
	}

	@Override
	public boolean update(Certificate certificate) {

		return false;
	}

	@Override
	public boolean delete(long id) {
		return false;
	}
}











