package com.epam.esm.entity;

import com.epam.esm.audit.AuditListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/** Represents order entity in the system */
@Entity
@EntityListeners(AuditListener.class)
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_id_seq")
  @SequenceGenerator(name = "orders_id_seq", allocationSize = 1)
  private Long id;

  private BigDecimal cost;

  @Column(
      columnDefinition = "timestamp(0) with time zone DEFAULT CURRENT_TIMESTAMP",
      insertable = false)
  private ZonedDateTime creationDate;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(columnDefinition = "boolean DEFAULT false", insertable = false)
  private boolean deleted;

  @ManyToMany
  @JoinTable(
      name = "orders_certificates",
      joinColumns = @JoinColumn(name = "order_id"),
      inverseJoinColumns = @JoinColumn(name = "certificate_id"))
  private List<Certificate> certificates;

  public Order() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public BigDecimal getCost() {
    return cost;
  }

  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }

  public ZonedDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(ZonedDateTime creationDate) {
    this.creationDate = creationDate;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public List<Certificate> getCertificates() {
    return certificates;
  }

  public void setCertificates(List<Certificate> certificates) {
    this.certificates = certificates;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Order)) {
      return false;
    }
    Order order = (Order) o;
    return isDeleted() == order.isDeleted()
        && getId().equals(order.getId())
        && getCost().equals(order.getCost())
        && Objects.equals(getCreationDate(), order.getCreationDate())
        && Objects.equals(getUser(), order.getUser())
        && Objects.equals(getCertificates(), order.getCertificates());
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        getId(), getCost(), getCreationDate(), getUser(), isDeleted(), getCertificates());
  }
}
