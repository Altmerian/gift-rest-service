package com.epam.esm.specification.tag;

public interface TagSQLSpecification extends TagSpecification {
  String toSqlQuery();

  Object[] getParameters();
}
