package com.epam.esm.entity;

import com.epam.esm.audit.AuditListener;

import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@EntityListeners(AuditListener.class)
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

  private static final long serialVersionUID = -3579211911237417048L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_gen")
  private Long id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}

