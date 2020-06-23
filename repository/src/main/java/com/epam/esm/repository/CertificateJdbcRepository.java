package com.epam.esm.repository;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateMapper;
import com.epam.esm.specification.SQLSpecification;
import com.epam.esm.specification.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class CertificateJdbcRepository implements CertificateRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public CertificateJdbcRepository(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public long create(Certificate certificate) {
    String sqlInsert =
        "INSERT INTO certificates "
            + "(name, description, price, duration_in_days) "
            + "VALUES (?, ?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(
        con -> {
          PreparedStatement ps = con.prepareStatement(sqlInsert, new String[] {"id"});
          ps.setString(1, certificate.getName());
          ps.setString(2, certificate.getDescription());
          ps.setBigDecimal(3, certificate.getPrice());
          ps.setInt(4, certificate.getDurationInDays());
          return ps;
        },
        keyHolder);
    return Objects.requireNonNull(keyHolder.getKey()).longValue();
  }

  @Override
  public List<Certificate> getAll() {
    String sqlGetAll =
        "SELECT id, name, description, price, creation_date, modification_date, duration_in_days FROM certificates";
    return jdbcTemplate.query(sqlGetAll, new CertificateMapper());
  }

  @Override
  public Optional<Certificate> get(long id) {
    String sqlGetById =
        "SELECT id, name, description, price, creation_date, modification_date, duration_in_days FROM certificates WHERE id = ?";
    Certificate certificate;
    try {
      certificate =
          jdbcTemplate.queryForObject(sqlGetById, new Object[] {id}, new CertificateMapper());
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
    return Optional.ofNullable(certificate);
  }

  @Override
  public List<Certificate> query(Specification specification) {
    if (!(specification instanceof SQLSpecification)) {
      return Collections.emptyList();
    }
    SQLSpecification sqlSpecification = (SQLSpecification) specification;
    return jdbcTemplate.query(
        sqlSpecification.toSqlQuery(), sqlSpecification.getParameters(), new CertificateMapper());
  }

  @Override
  public void update(Certificate certificate) {
    String sqlUpdate =
        "UPDATE certificates SET name = ?, description = ?, price = ?, duration_in_days = ?, "
            + "modification_date = current_timestamp WHERE id = ?";
    jdbcTemplate.update(
        sqlUpdate,
        certificate.getName(),
        certificate.getDescription(),
        certificate.getPrice(),
        certificate.getDurationInDays(),
        certificate.getId());
  }

  @Override
  public void delete(Certificate certificate) {
    String sqlDelete = "DELETE FROM certificates WHERE id = ?";
    jdbcTemplate.update(sqlDelete, certificate.getId());
  }

  @Override
  public void addCertificateTag(long certificateId, long tagId) {
    String sqlAddCertificateTag =
        "INSERT INTO certificates_tags (certificate_id, tag_id) VALUES (?, ?)";
    jdbcTemplate.update(sqlAddCertificateTag, certificateId, tagId);
  }

  @Override
  public void clearCertificateTags(long certificateId) {
    String sqlClearCertificateTags = "DELETE FROM certificates_tags WHERE certificate_id = ?";
    jdbcTemplate.update(sqlClearCertificateTags, certificateId);
  }
}
