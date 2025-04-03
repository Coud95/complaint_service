package com.empik.complaint_service.infrastructure.repository;

import com.empik.complaint_service.infrastructure.entity.ComplaintEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintJpaRepository extends JpaRepository<ComplaintEntity, Long> { }
