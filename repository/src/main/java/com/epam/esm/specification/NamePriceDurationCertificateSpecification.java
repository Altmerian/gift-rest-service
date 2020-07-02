package com.epam.esm.specification;

import com.epam.esm.entity.Certificate;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;

public class NamePriceDurationCertificateSpecification implements Specification<Certificate> {
  private final String name;
  private final BigDecimal price;
  private final int duration;

  public NamePriceDurationCertificateSpecification(String name, BigDecimal price, int duration) {
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

  @Override
  public TypedQuery<Certificate> toJPAQuery(EntityManager entityManager) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Certificate> cq = cb.createQuery(Certificate.class);
    Root<Certificate> certificate = cq.from(Certificate.class);
    Predicate predicate = cb.and(
        cb.equal(certificate.get("name"), name),
        cb.equal(certificate.get("price"), price),
        cb.equal(certificate.get("durationInDays"), duration));
    cq.where(predicate);
    return entityManager.createQuery(cq);
  }
}
