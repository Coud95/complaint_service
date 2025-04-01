package com.empik.complaint_service.infrastructure.repository;

import com.empik.complaint_service.infrastructure.Submitter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmitterRepository extends JpaRepository<Submitter, Long> {
}
