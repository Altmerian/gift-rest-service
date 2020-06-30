package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import com.epam.esm.specification.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class TagJPARepository implements TagRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<Tag> getAll() {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tag> cq = cb.createQuery(Tag.class);
    Root<Tag> rootEntry = cq.from(Tag.class);
    CriteriaQuery<Tag> all = cq.select(rootEntry);

    TypedQuery<Tag> allQuery = entityManager.createQuery(all);
    return allQuery.getResultList();
  }

  @Override
  public Optional<Tag> get(long id) {
    Tag tag = entityManager.find(Tag.class, id);
    return Optional.ofNullable(tag);
  }

  @Override
  public long create(Tag tag) {
    entityManager.persist(tag);
    entityManager.flush();
    return tag.getId();
  }

  @Override
  public void delete(Tag tag) {
    entityManager.remove(tag);
  }

  //todo check this
  @Override
  public List<Tag> query(Specification<Tag> specification) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(specification.getType());
    Root<Tag> root = criteriaQuery.from(specification.getType());

    Predicate predicate = specification.toPredicate(root, criteriaBuilder);

    criteriaQuery.where(predicate);
    return entityManager.createQuery(criteriaQuery).getResultList();
  }

  @Override
  public boolean contains(Tag tag) {
    String containsJpql = "SELECT t.id, t.name FROM Tag AS t WHERE t.name = :name";
    return !entityManager.createQuery(containsJpql).setParameter("name", tag.getName()).getResultList().isEmpty();
  }
}
