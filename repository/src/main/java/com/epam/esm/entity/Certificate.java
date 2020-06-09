package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Certificate {

    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime creationDate;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime modificationDate;
    private int durationInDays;
    private List<Tag> tags;
    private boolean isDeleted = false;

    public Certificate() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(LocalDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(int durationInDays) {
        this.durationInDays = durationInDays;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Certificate)) return false;

        Certificate that = (Certificate) o;

        if (!getName().equals(that.getName())) return false;
        if (!getPrice().equals(that.getPrice())) return false;
        if (getDurationInDays() != that.getDurationInDays()) return false;
        if (isDeleted() != that.isDeleted()) return false;
        return getTags() != null ? getTags().equals(that.getTags()) : that.getTags() == null;
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getPrice().hashCode();
        result = 31 * result + getDurationInDays();
        result = 31 * result + (getTags() != null ? getTags().hashCode() : 0);
        result = 31 * result + (isDeleted() ? 1 : 0);
        return result;
    }
}





