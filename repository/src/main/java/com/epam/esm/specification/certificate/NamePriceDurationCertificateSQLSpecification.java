package com.epam.esm.specification.certificate;

import java.math.BigDecimal;

public class NamePriceDurationCertificateSQLSpecification implements CertificateSQLSpecification {
  private final String name;
  private final BigDecimal price;
  private final int duration;

  public NamePriceDurationCertificateSQLSpecification(String name, BigDecimal price, int duration) {
    this.name = name;
    this.price = price;
    this.duration = duration;
  }

  @Override
  public String toSqlQuery() {
    return String.format(
        "SELECT * FROM certificates WHERE name = '%s' AND price = %f AND duration_in_days = %d",
        name, price, duration);
  }
}
