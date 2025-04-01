package com.empik.complaint_service.infrastructure;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "submitter")
public class Submitter {

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

    @OneToMany
    @JoinColumn(name = "complaintId", referencedColumnName = "id")
    private List<Complaint> complaints;

}
