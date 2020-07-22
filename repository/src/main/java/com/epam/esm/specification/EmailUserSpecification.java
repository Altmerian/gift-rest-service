package com.epam.esm.specification;

import com.epam.esm.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class EmailUserSpecification implements Specification<User> {
  private final String email;

  public EmailUserSpecification(String email) {
    this.email = email;
  }

  @Override
  public String toSqlQuery() {
    return "SELECT id, email FROM users WHERE email = ?";
  }

  @Override
  public Object[] getParameters() {
    return new Object[] {email};
  }

  @Override
  public Query toJPAQuery(EntityManager entityManager) {
    throw new UnsupportedOperationException();
  }

  @Override
  public TypedQuery<User> typedJPAQuery(EntityManager entityManager) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<User> cq = cb.createQuery(User.class);
    Root<User> user = cq.from(User.class);
    Predicate predicate = cb.equal(user.get("email"), email);
    Predicate predicate1 = cb.notEqual(user.get("deleted"), true);
    cq.where(cb.and(predicate, predicate1));
    return entityManager.createQuery(cq);
  }
}
