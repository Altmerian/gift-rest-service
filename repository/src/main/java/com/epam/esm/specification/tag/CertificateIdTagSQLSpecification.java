package com.epam.esm.specification.tag;

public class CertificateIdTagSQLSpecification implements TagSQLSpecification {
  private final long certificate_id;

  public CertificateIdTagSQLSpecification(long certificate_id) {
    this.certificate_id = certificate_id;
  }

  @Override
  public String toSqlQuery() {
    return String.format(
        "SELECT id, name FROM tags LEFT JOIN certificates_tags ON id = tag_id WHERE certificate_id = %d",
        certificate_id);
  }
}
