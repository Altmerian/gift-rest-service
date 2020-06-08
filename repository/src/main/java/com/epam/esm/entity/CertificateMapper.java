package com.epam.esm.entity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CertificateMapper implements RowMapper<Certificate> {

    @Override
    public Certificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        Certificate certificate = new Certificate();
        certificate.setId(rs.getInt("id"));
        certificate.setOwnerName(rs.getString("owner_name"));
        certificate.setDescription(rs.getString("description"));
        certificate.setPrice(rs.getBigDecimal("price"));
        certificate.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
        if (rs.getTimestamp("modification_date") != null) {
            certificate.setModificationDate(
                    rs.getTimestamp("modification_date").toLocalDateTime());
        }
        certificate.setExpirationDate(
                rs.getTimestamp("expiration_date").toLocalDateTime().toLocalDate());
        return certificate;
    }
}
