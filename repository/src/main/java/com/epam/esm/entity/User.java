package com.epam.esm.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Set;

/** Represents user of the system */
@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
  @SequenceGenerator(name = "users_id_seq", allocationSize = 1)
  private Long id;
  private String email;
  private String password;
  private String firstName;
  private String lastName;
  private UserRole userRole;

  @OneToMany(mappedBy = "user")
  private Set<Order> orders;
}
