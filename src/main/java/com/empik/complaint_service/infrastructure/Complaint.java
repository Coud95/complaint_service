package com.empik.complaint_service.infrastructure;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "customer")
public class Complaint {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long complaintId;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "submitterId", referencedColumnName = "id")
    private Submitter submitter;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "submit_count", nullable = false)
    private String submitCount;


}
