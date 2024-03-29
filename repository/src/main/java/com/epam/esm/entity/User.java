package com.epam.esm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;
import java.util.Set;

/** Represents user of the system */
@Entity
@Table(name = "users")
@SequenceGenerator(name = "id_seq_gen", sequenceName = "users_id_seq", allocationSize = 1)
public class User extends BaseEntity {

  private static final long serialVersionUID = 873436319547785490L;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof User)) {
      return false;
    }
    User user = (User) o;
    return isDeleted() == user.isDeleted()
        && getId().equals(user.getId())
        && Objects.equals(getEmail(), user.getEmail())
        && Objects.equals(getPassword(), user.getPassword())
        && Objects.equals(getFirstName(), user.getFirstName())
        && Objects.equals(getLastName(), user.getLastName())
        && getUserRole() == user.getUserRole()
        && Objects.equals(getOrders(), user.getOrders());
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        getId(),
        getEmail(),
        getPassword(),
        getFirstName(),
        getLastName(),
        getUserRole(),
        isDeleted(),
        getOrders());
  }
}
