package com.epam.esm.repository;

import com.epam.esm.entity.Certificate;
import com.epam.esm.specification.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class CertificateJPARepository extends AbstractRepository<Certificate>
    implements CertificateRepository {

  @PersistenceContext private EntityManager entityManager;

  @Autowired
  public CertificateJPARepository(EntityManager entityManager) {
    super(entityManager, Certificate.class);
  }

  @Override
  public long countAll(Specification<Certificate> specification) {
    Query query = specification.toJPAQuery(entityManager);
    return query.getResultStream().count();
  }

  @Override
  public List<Certificate> query(Specification<Certificate> specification, int page, int size) {
    TypedQuery<Certificate> typedQuery = specification.toJPAQuery(entityManager);
    typedQuery.setFirstResult((page - 1) * size);
    typedQuery.setMaxResults(size);
    return typedQuery.getResultList();
  }

  @Override
  @Transactional
  public void delete(Certificate certificate) {
    certificate.setDeleted(true);
    entityManager.merge(certificate);
  }
}
