package com.epam.esm.entity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;

public class CertificateMapper implements RowMapper<Certificate> {

  @Override
  public Certificate mapRow(ResultSet rs, int rowNum) throws SQLException {
    Certificate certificate = new Certificate();
    certificate.setId(rs.getLong("id"));
    certificate.setName(rs.getString("name"));
    certificate.setDescription(rs.getString("description"));
    certificate.setPrice(rs.getBigDecimal("price"));
    certificate.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime().atZone(ZoneId.systemDefault()));
    if (rs.getTimestamp("modification_date") != null) {
      certificate.setModificationDate(rs.getTimestamp("modification_date").toLocalDateTime().atZone(ZoneId.systemDefault()));
    }
    certificate.setDurationInDays(rs.getInt("duration_in_days"));
    return certificate;
  }
}
