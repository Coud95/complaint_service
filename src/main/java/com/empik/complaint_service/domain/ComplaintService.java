package com.empik.complaint_service.domain;

import com.empik.complaint_service.infrastructure.entity.ComplaintEntity;
import com.empik.complaint_service.infrastructure.entity.SubmitterEntity;
import com.empik.complaint_service.infrastructure.mapper.ComplaintMapper;
import com.empik.complaint_service.infrastructure.repository.ComplaintRepository;
import com.empik.complaint_service.infrastructure.repository.SubmitterRepository;
import com.empik.complaint_service.model.Complaint;
import com.empik.complaint_service.model.EditComplaint;
import com.empik.complaint_service.model.Submitter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final SubmitterRepository submitterRepository;

    public ComplaintService(ComplaintRepository complaintRepository, SubmitterRepository submitterRepository) {
        this.complaintRepository = complaintRepository;
        this.submitterRepository = submitterRepository;
    }

    public Complaint addComplaint(Complaint complaint) {
        SubmitterEntity submitterEntity = getOrCreateSubmitter(complaint);
        Optional<ComplaintEntity> duplicatedComplaintEntity = submitterEntity.getComplaintEntities().stream()
                .filter(complaintEntity -> complaintEntity.getProductId().equals(complaint.getProductId()))
                .findFirst();
        if (duplicatedComplaintEntity.isPresent()) {
            ComplaintEntity existingComplaint = duplicatedComplaintEntity.get();
            existingComplaint.setSubmitCount(existingComplaint.getSubmitCount() + 1);
            complaintRepository.save(existingComplaint);
            return ComplaintMapper.mapToModel(existingComplaint);
        }
        ComplaintEntity complaintEntity = new ComplaintEntity(complaint.getProductId(), complaint.getDescription(), LocalDate.now(), submitterEntity, "Poland", 1);
        List<ComplaintEntity> complaintEntities = submitterEntity.getComplaintEntities();
        complaintEntities.add(complaintEntity);
        submitterEntity.setComplaintEntities(complaintEntities);
        ComplaintEntity savedComplaint = complaintRepository.save(complaintEntity);
        return ComplaintMapper.mapToModel(savedComplaint);
    }

    public Optional<Complaint> editComplaint(EditComplaint editComplaint) {
        Optional<ComplaintEntity> existingComplaint = complaintRepository.findById(editComplaint.getComplaintId());
        if (existingComplaint.isPresent()) {
            ComplaintEntity complaintEntity = existingComplaint.get();
            complaintEntity.setDescription(editComplaint.getDescription());
            ComplaintEntity savedComplaint = complaintRepository.save(complaintEntity);
            Complaint mappedComplaint = ComplaintMapper.mapToModel(savedComplaint);
            return Optional.of(mappedComplaint);
        }
        return Optional.empty();
    }

    public Optional<Complaint> getComplaintById(Long id) {
        Optional<ComplaintEntity> complaintEntity = complaintRepository.findById(id);
        if (complaintEntity.isPresent()) {
            Complaint mappedComplaint = ComplaintMapper.mapToModel(complaintEntity.get());
            return Optional.of(mappedComplaint);
        }
        return Optional.empty();
    }

    private SubmitterEntity getOrCreateSubmitter(Complaint complaint) {
        Optional<SubmitterEntity> existingSubmitter = submitterRepository.findByEmailAddress(complaint.getSubmitter().getEmailAddress());
        return existingSubmitter.orElseGet(() -> {
            Submitter newSubmitter = complaint.getSubmitter();
            SubmitterEntity newSubmitterEntity = new SubmitterEntity(newSubmitter.getFirstName(), newSubmitter.getLastName(), newSubmitter.getEmailAddress());
            return submitterRepository.save(newSubmitterEntity);
        });
    }
}
