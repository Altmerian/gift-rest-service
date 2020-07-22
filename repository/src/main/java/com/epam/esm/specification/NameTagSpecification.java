package com.epam.esm.specification;

import com.epam.esm.entity.Tag;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class NameTagSpecification implements Specification<Tag> {
  private final String name;

  public NameTagSpecification(String name) {
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

  @Override
  public Query toJPAQuery(EntityManager entityManager) {
    throw new UnsupportedOperationException();
  }

  @Override
  public TypedQuery<Tag> typedJPAQuery(EntityManager entityManager) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tag> cq = cb.createQuery(Tag.class);
    Root<Tag> tag = cq.from(Tag.class);
    Predicate predicate = cb.equal(tag.get("name"), name);
    cq.where(predicate);
    return entityManager.createQuery(cq);
  }
}
