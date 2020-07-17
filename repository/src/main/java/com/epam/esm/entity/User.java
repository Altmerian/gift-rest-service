package com.epam.esm.entity;

import com.epam.esm.audit.AuditListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Set;

/** Represents user of the system */
@Entity
@EntityListeners(AuditListener.class)
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

  @Enumerated(EnumType.STRING)
  @Column(insertable = false, updatable = false)
  private UserRole userRole;

  @Column(columnDefinition = "boolean DEFAULT false", insertable = false)
  private boolean deleted;

  @OneToMany(mappedBy = "user")
  private Set<Order> orders;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public UserRole getUserRole() {
    return userRole;
  }

  public void setUserRole(UserRole userRole) {
    this.userRole = userRole;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public Set<Order> getOrders() {
    return orders;
  }

  public void setOrders(Set<Order> orders) {
    this.orders = orders;
  }
}
