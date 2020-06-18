package com.epam.esm.specification.tag;

public class NameTagSQLSpecification implements TagSQLSpecification {
  private final String name;

  public NameTagSQLSpecification(String name) {
    this.name = name;
  }

  @Override
  public String toSqlQuery() {
    return "SELECT id, name FROM tags WHERE name = ?";
  }

  @Override
  public Object[] getParameters() {
    return new Object[] {name};
  }
}
