package com.epam.esm.specification.certificate;

public interface CertificateSQLSpecification extends CertificateSpecification {

  String toSqlQuery();

  Object[] getParameters();
}
