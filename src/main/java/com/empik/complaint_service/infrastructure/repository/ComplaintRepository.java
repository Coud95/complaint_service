package com.empik.complaint_service.infrastructure.repository;

import com.empik.complaint_service.infrastructure.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> { }
