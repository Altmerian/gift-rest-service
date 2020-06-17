package com.epam.esm.specification.certificate;

public interface CertificateSQLSpecification extends CertificateSpecification {
  public String toSqlQuery();
}
