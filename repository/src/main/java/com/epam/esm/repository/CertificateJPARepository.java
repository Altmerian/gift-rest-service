package com.epam.esm.repository;

import com.epam.esm.entity.Certificate;
import com.epam.esm.specification.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class CertificateJPARepository implements CertificateRepository {

  @PersistenceContext private EntityManager entityManager;

  @Override
  public long create(Certificate certificate) {
    entityManager.persist(certificate);
    entityManager.flush();
    return certificate.getId();
  }

  @Override
  public List<Certificate> getAll(int page, int size) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Certificate> cq = cb.createQuery(Certificate.class);
    Root<Certificate> rootEntry = cq.from(Certificate.class);
    cq.orderBy(cb.asc(rootEntry.get("id")));
    CriteriaQuery<Certificate> all = cq.select(rootEntry);
    cq.where(cb.notEqual(rootEntry.get("deleted"), true));

    TypedQuery<Certificate> allQuery = entityManager.createQuery(all);
    allQuery.setFirstResult((page - 1) * size);
    allQuery.setMaxResults(size);
    return allQuery.getResultList();
  }

  @Override
  public long countAll(Specification<Certificate> specification) {
    Query query = specification.toJPAQuery(entityManager);
    return query.getResultStream().count();
  }

  @Override
  public Optional<Certificate> get(long id) {
    Certificate certificate = entityManager.find(Certificate.class, id);
    return Optional.ofNullable(certificate);
  }

  @Override
  public List<Certificate> query(Specification<Certificate> specification, int page, int size) {
    Query query = specification.toJPAQuery(entityManager);
    query.setFirstResult((page - 1) * size);
    query.setMaxResults(size);
    @SuppressWarnings("unchecked")
    List<Certificate> resultList = (List<Certificate>) query.getResultList();
    return resultList;
  }

  @Override
  public List<Certificate> query(Specification<Certificate> specification) {
    return query(specification,1, 10);
  }

  @Override
  public void update(Certificate certificate) {
    entityManager.merge(certificate);
  }

  @Override
  public void delete(Certificate certificate) {
    certificate.setDeleted(true);
    entityManager.merge(certificate);
  }
}
