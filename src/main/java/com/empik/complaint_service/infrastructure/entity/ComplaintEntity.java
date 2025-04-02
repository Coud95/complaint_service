package com.empik.complaint_service.infrastructure.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "complaint")
public class ComplaintEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long complaintId;

    @Column(name = "productId", nullable = false)
    private Long productId;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "submitterId", referencedColumnName = "id")
    private SubmitterEntity submitterEntity;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "submit_count", nullable = false)
    private Integer submitCount;

    public ComplaintEntity() {
    }

    public ComplaintEntity(Long productId, String description, LocalDate creationDate, SubmitterEntity submitterEntity, String country,
                           Integer submitCount) {
        this.productId = productId;
        this.description = description;
        this.creationDate = creationDate;
        this.submitterEntity = submitterEntity;
        this.country = country;
        this.submitCount = submitCount;
    }

    public Long getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(Long complaintId) {
        this.complaintId = complaintId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public SubmitterEntity getSubmitterEntity() {
        return submitterEntity;
    }

    public void setSubmitterEntity(SubmitterEntity submitterEntity) {
        this.submitterEntity = submitterEntity;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getSubmitCount() {
        return submitCount;
    }

    public void setSubmitCount(Integer submitCount) {
        this.submitCount = submitCount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ComplaintEntity that = (ComplaintEntity) o;
        return Objects.equals(productId, that.productId) && Objects.equals(submitterEntity, that.submitterEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, submitterEntity);
    }
}
