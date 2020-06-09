package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class Tag {
    private long id;
    private String name;
    @JsonIgnore
    private List<Certificate> certificates;

    public Tag() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<Certificate> certificates) {
        this.certificates = certificates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;

        Tag tag = (Tag) o;

        return getName().equals(tag.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
