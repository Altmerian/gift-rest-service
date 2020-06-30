package com.epam.esm.repository;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.specification.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class CertificateJPARepository implements CertificateRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public long create(Certificate certificate) {
    certificate.setCreationDate(ZonedDateTime.now());
    entityManager.persist(certificate);
    entityManager.flush();
    return certificate.getId();
  }

  @Override
  public List<Certificate> getAll() {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Certificate> cq = cb.createQuery(Certificate.class);
    Root<Certificate> rootEntry = cq.from(Certificate.class);
    CriteriaQuery<Certificate> all = cq.select(rootEntry);

    TypedQuery<Certificate> allQuery = entityManager.createQuery(all);
    return allQuery.getResultList();
  }

  @Override
  public Optional<Certificate> get(long id) {
    Certificate certificate = entityManager.find(Certificate.class, id);
    return Optional.ofNullable(certificate);
  }

  //todo
  @Override
  public List<Certificate> query(Specification<Certificate> specification) {
    return Collections.emptyList();
  }

  @Override
  public void update(Certificate certificate) {
    entityManager.merge(certificate);
  }

  @Override
  public void delete(Certificate certificate) {
    entityManager.remove(certificate);
  }

  @Override
  public void addCertificateTag(long certificateId, long tagId) {
    Certificate certificate = entityManager.find(Certificate.class, certificateId);
    Tag tag = entityManager.find(Tag.class, tagId);
    certificate.addTag(tag);
    entityManager.flush();
  }

  @Override
  public void clearCertificateTags(long certificateId) {
    Certificate certificate = entityManager.find(Certificate.class, certificateId);
    certificate.setTags(Collections.emptySet());
    entityManager.flush();
  }
}
