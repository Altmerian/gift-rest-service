package com.epam.esm.repository;

import com.epam.esm.entity.User;
import com.epam.esm.specification.EmailUserSpecification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserJPARepository extends AbstractRepository<User> implements UserRepository{

  @PersistenceContext private EntityManager entityManager;

  public UserJPARepository(EntityManager entityManager) {
    super(entityManager, User.class);
  }

  @Override
  public boolean contains(User user) {
    return !query(new EmailUserSpecification(user.getEmail())).isEmpty();
  }

  @Override
  @Transactional
  public void delete(User user) {
    user.setDeleted(true);
    entityManager.merge(user);
  }
}
