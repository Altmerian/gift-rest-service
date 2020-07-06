package com.epam.esm.repository;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateMapper;
import com.epam.esm.specification.CertificateIdTagSpecification;
import com.epam.esm.specification.Specification;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class CertificateJdbcRepository implements CertificateRepository {

  private final JdbcTemplate jdbcTemplate;
  private final TagRepository tagRepository;

  @Autowired
  public CertificateJdbcRepository(
      DataSource dataSource, @Qualifier("tagJdbcRepository") TagRepository tagRepository) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.tagRepository = tagRepository;
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
    long certificateId = Objects.requireNonNull(keyHolder.getKey()).longValue();
    certificate.getTags().forEach(tag -> addCertificateTag(certificateId, tag.getId()));
    return certificateId;
  }

  @Override
  public List<Certificate> getAll(int page, int size) {
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
    Objects.requireNonNull(certificate);
    CertificateIdTagSpecification tagSpecification =
        new CertificateIdTagSpecification(certificate.getId());
    certificate.setTags(new HashSet<>(tagRepository.query(tagSpecification)));
    return Optional.of(certificate);
  }

  @Override
  public List<Certificate> query(Specification<Certificate> specification, int page, int size) {
    throw new NotImplementedException();
  }

  @Override
  public List<Certificate> query(Specification<Certificate> specification) {
    return jdbcTemplate.query(
        specification.toSqlQuery(), specification.getParameters(), new CertificateMapper());
  }

  @Override
  public long countAll(Specification<Certificate> specification) {
    throw new NotImplementedException();
  }

  @Override
  public void update(Certificate certificate) {
    clearCertificateTags(certificate.getId());
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
    long certificateId = certificate.getId();
    certificate.getTags().forEach(tag -> addCertificateTag(certificateId, tag.getId()));
  }

  @Override
  public void delete(Certificate certificate) {
    String sqlDelete = "DELETE FROM certificates WHERE id = ?";
    jdbcTemplate.update(sqlDelete, certificate.getId());
  }

  /**
   * Saves data about certificate tag in the repository
   *
   * @param certificateId certificate id
   * @param tagId tag id
   */
  @VisibleForTesting
  void addCertificateTag(long certificateId, long tagId) {
    String sqlAddCertificateTag =
        "INSERT INTO certificates_tags (certificate_id, tag_id) VALUES (?, ?)";
    jdbcTemplate.update(sqlAddCertificateTag, certificateId, tagId);
  }

  /**
   * Purges data in repository about certificate tags
   *
   * @param certificateId certificate id
   */
  @VisibleForTesting
  public void clearCertificateTags(long certificateId) {
    String sqlClearCertificateTags = "DELETE FROM certificates_tags WHERE certificate_id = ?";
    jdbcTemplate.update(sqlClearCertificateTags, certificateId);
  }
}
