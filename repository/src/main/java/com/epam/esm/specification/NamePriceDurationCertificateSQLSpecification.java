package com.epam.esm.specification;

import java.math.BigDecimal;

public class NamePriceDurationCertificateSQLSpecification implements SQLSpecification {
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
    return "SELECT id, name, description, price, creation_date, modification_date, duration_in_days FROM certificates WHERE name = ? AND price = ? AND duration_in_days = ?";
  }

  @Override
  public Object[] getParameters() {
    return new Object[] {name, price, duration};
  }
}
