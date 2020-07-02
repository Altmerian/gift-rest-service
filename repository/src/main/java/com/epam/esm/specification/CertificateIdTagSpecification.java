package com.epam.esm.specification;

import com.epam.esm.entity.Tag;
import org.apache.commons.lang3.NotImplementedException;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class CertificateIdTagSpecification implements Specification<Tag> {
  private final long certificate_id;

  public CertificateIdTagSpecification(long certificate_id) {
    this.certificate_id = certificate_id;
  }

  @Override
  public String toSqlQuery() {
    return "SELECT id, name FROM tags LEFT JOIN certificates_tags ON id = tag_id WHERE certificate_id = ?";
  }

  @Override
  public Object[] getParameters() {
    return new Object[]{certificate_id};
  }

  @Override
  public Query toJPAQuery(EntityManager entityManager) {
    throw new NotImplementedException();
  }
}
