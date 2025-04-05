package com.empik.complaint_service.infrastructure.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "submitter", indexes = @Index(columnList = "email_address"))
public class SubmitterEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long submitterId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "submitterEntity", cascade = CascadeType.ALL)
    private List<ComplaintEntity> complaintEntities;

    public SubmitterEntity() {
    }

    public SubmitterEntity(String firstName, String lastName, String emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
    }

    public Long getSubmitterId() {
        return submitterId;
    }

    public void setSubmitterId(Long submitterId) {
        this.submitterId = submitterId;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public List<ComplaintEntity> getComplaintEntities() {
        if (complaintEntities == null) {
            complaintEntities = new ArrayList<>();
        }
        return complaintEntities;
    }

    public void setComplaintEntities(List<ComplaintEntity> complaintEntities) {
        this.complaintEntities = complaintEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SubmitterEntity that = (SubmitterEntity) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(emailAddress, that.emailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, emailAddress);
    }
}
