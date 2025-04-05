package com.empik.complaint_service.infrastructure.repository;

import com.empik.complaint_service.infrastructure.entity.ComplaintEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintRepository extends JpaRepository<ComplaintEntity, Long> { }
