package com.epam.esm.specification;

import com.epam.esm.entity.Tag;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class CertificateIdTagSpecification implements Specification<Tag> {
  private final long certificateId;

  public CertificateIdTagSpecification(long certificateId) {
    this.certificateId = certificateId;
  }

  @Override
  public String toSqlQuery() {
    return "SELECT id, name FROM tags LEFT JOIN certificates_tags ON id = tag_id WHERE certificate_id = ?";
  }

  @Override
  public Object[] getParameters() {
    return new Object[]{certificateId};
  }

  @Override
  public TypedQuery<Tag> toJPAQuery(EntityManager entityManager) {
    throw new UnsupportedOperationException();
  }

}
