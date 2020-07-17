package com.epam.esm.entity;


import com.epam.esm.audit.Operation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "events")
public class Event {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "events_id_seq")
  @SequenceGenerator(name = "events_id_seq", allocationSize = 1)
  private Long id;

  @Enumerated(EnumType.STRING)
  private Operation operation;

  @Column(
      columnDefinition = "timestamp(0) with time zone DEFAULT CURRENT_TIMESTAMP",
      insertable = false)
  private ZonedDateTime timestamp;

  private String entityType;
  private Long entityId;
  private Long userId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Operation getOperation() {
    return operation;
  }

  public void setOperation(Operation operation) {
    this.operation = operation;
  }

  public ZonedDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(ZonedDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public String getEntityType() {
    return entityType;
  }

  public void setEntityType(String entityType) {
    this.entityType = entityType;
  }

  public Long getEntityId() {
    return entityId;
  }

  public void setEntityId(Long entityId) {
    this.entityId = entityId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }
}
