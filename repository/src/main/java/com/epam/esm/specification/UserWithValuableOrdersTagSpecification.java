package com.epam.esm.specification;

import com.epam.esm.entity.Tag;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class UserWithValuableOrdersTagSpecification implements Specification<Tag> {

  @Override
  public String toSqlQuery() {
    return "SELECT id, name FROM user_tags WHERE tag_count = (SELECT MAX(tag_count) FROM user_tags)";
  }

  @Override
  public Object[] getParameters() {
    throw new NotImplementedException();
  }

  @Override
  public Query toJPAQuery(EntityManager entityManager) {
    return entityManager.createNativeQuery(toSqlQuery(), Tag.class);
  }
}
