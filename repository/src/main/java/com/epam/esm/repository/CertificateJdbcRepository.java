package com.epam.esm.repository;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateMapper;
import com.epam.esm.util.QueryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
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
  public List<Certificate> getAll(String tagName, String searchFor, String sortBy) {
    String sortQuery = QueryHelper.getSortQuery(sortBy);
    String sqlGetAll;
    if (tagName == null && searchFor == null) {
      sqlGetAll = String.format("SELECT * FROM certificates ORDER BY %s", sortQuery);
    } else {
      tagName = tagName == null ? "%" : tagName.trim().split(",")[0];
      searchFor = searchFor == null ? "%" : searchFor.trim().split(",")[0];
      sqlGetAll =
          String.format(
              "SELECT * FROM certificates_function('%s', '%s') ORDER BY %s",
              tagName, searchFor, sortQuery);
    }
    return jdbcTemplate.query(sqlGetAll, new CertificateMapper());
  }

  @Override
  public Optional<Certificate> getById(long id) {
    String sqlGetById = "SELECT * FROM certificates WHERE id = ?";
    Certificate certificate;
    try {
      certificate =
          jdbcTemplate.queryForObject(sqlGetById, new Object[] {id}, new CertificateMapper());
    } catch (IncorrectResultSizeDataAccessException e) {
      return Optional.empty();
    }
    return Optional.ofNullable(certificate);
  }

  @Override
  public Optional<Certificate> getByNameDurationPrice(
      String name, int durationInDays, BigDecimal price) {
    String sqlGetByNameDurationPrice =
        "SELECT * FROM certificates WHERE name = ? AND price = ? AND duration_in_days = ?";
    Certificate certificate;
    try {
      certificate =
          jdbcTemplate.queryForObject(
              sqlGetByNameDurationPrice,
              new Object[] {name, price, durationInDays},
              new CertificateMapper());
    } catch (IncorrectResultSizeDataAccessException e) {
      return Optional.empty();
    }
    return Optional.ofNullable(certificate);
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
  public void addCertificateTag(long certificateId, long tagId) {
    String sqlAddCertificateTag =
        "INSERT INTO certificates_tags (certificate_id, tag_id) VALUES (?, ?)";
    jdbcTemplate.update(sqlAddCertificateTag, certificateId, tagId);
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
    String sqlDelete = "delete from certificates where id = ?";
    jdbcTemplate.update(sqlDelete, certificate.getId());
  }
}
