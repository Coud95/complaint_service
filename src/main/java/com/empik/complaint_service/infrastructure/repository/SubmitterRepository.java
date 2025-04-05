package com.empik.complaint_service.infrastructure.repository;

import com.empik.complaint_service.infrastructure.entity.SubmitterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubmitterRepository extends JpaRepository<SubmitterEntity, Long> {
    Optional<SubmitterEntity> findByEmailAddress(String emailAddress);
}
