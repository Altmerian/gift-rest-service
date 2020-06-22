package com.epam.esm.specification.certificate;

/**
 * Used to construct SQL-based query from relational database-backed repositories. Query parameters
 * are set as fields during interface implementation.
 */
public interface CertificateSQLSpecification extends CertificateSpecification {

  /**
   * Constructs SQL-query according given parameters
   *
   * @return proper SQL-query
   */
  String toSqlQuery();

  /**
   * Creates new {@code Object[]} from specification parameters and returns it.
   *
   * @return array of specification parameters
   */
  Object[] getParameters();
}
