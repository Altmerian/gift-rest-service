package com.epam.esm.repository;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CertificateJdbcRepository implements CertificateRepository {

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert simpleJdbcInsert;

  @Autowired
  public CertificateJdbcRepository(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.simpleJdbcInsert =
        new SimpleJdbcInsert(dataSource)
            .withTableName("certificates")
            .usingColumns("name", "description", "price", "duration_in_days")
            .usingGeneratedKeyColumns("id");
  }

  @Override
  public List<Certificate> getAll() {
    String SQL_GET_ALL = "SELECT * FROM certificates";
    return jdbcTemplate.query(SQL_GET_ALL, new CertificateMapper());
  }

  @Override
  public Optional<Certificate> getById(long id) {
    String SQL_GET_BY_ID = "SELECT * FROM certificates WHERE id = ?";
    Certificate certificate;
    try {
      certificate =
          jdbcTemplate.queryForObject(SQL_GET_BY_ID, new Object[] {id}, new CertificateMapper());
    } catch (IncorrectResultSizeDataAccessException e) {
      return Optional.empty();
    }
    return Optional.ofNullable(certificate);
  }

  @Override
  public Optional<Certificate> getByNameDurationPrice(
      String name, int durationInDays, BigDecimal price) {
    String SQL_GET_BY_ID =
        "SELECT * FROM certificates " + "WHERE name = ? AND price = ? AND duration_in_days = ?";
    Certificate certificate;
    try {
      certificate =
          jdbcTemplate.queryForObject(
              SQL_GET_BY_ID, new Object[] {name, price, durationInDays}, new CertificateMapper());
    } catch (IncorrectResultSizeDataAccessException e) {
      return Optional.empty();
    }
    return Optional.ofNullable(certificate);
  }

  @Override
  public long create(Certificate certificate) {
    Map<String, Object> parameters = new HashMap<>(4);
    parameters.put("name", certificate.getName());
    parameters.put("description", certificate.getDescription());
    parameters.put("price", certificate.getPrice());
    parameters.put("duration_in_days", certificate.getDurationInDays());
    Number certificateId = simpleJdbcInsert.executeAndReturnKey(parameters);
    return certificateId.longValue();
  }

  @Override
  public void addCertificateTag(long certificateId, long tagId) {
    String SQL_ADD_CERTIFICATE_TAG =
        "INSERT INTO certificates_tags (certificate_id, tag_id) VALUES (?, ?)";
    jdbcTemplate.update(SQL_ADD_CERTIFICATE_TAG, certificateId, tagId);
  }

  @Override
  public void update(long id, Certificate certificate) {
    String SQL_UPDATE =
        "UPDATE certificates SET name = ?, description = ?, price = ?, duration_in_days = ?, " +
            "modification_date = current_timestamp WHERE id = ?";
    jdbcTemplate.update(
        SQL_UPDATE,
        certificate.getName(),
        certificate.getDescription(),
        certificate.getPrice(),
        certificate.getDurationInDays(),
        id);
  }

  @Override
  public void delete(long id) {}
}
