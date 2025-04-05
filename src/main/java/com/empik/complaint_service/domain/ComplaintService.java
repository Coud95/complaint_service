package com.empik.complaint_service.domain;

import com.empik.complaint_service.infrastructure.entity.ComplaintEntity;
import com.empik.complaint_service.infrastructure.entity.SubmitterEntity;
import com.empik.complaint_service.infrastructure.mapper.ComplaintMapper;
import com.empik.complaint_service.infrastructure.repository.ComplaintRepository;
import com.empik.complaint_service.infrastructure.repository.SubmitterRepository;
import com.empik.complaint_service.model.Complaint;
import com.empik.complaint_service.model.EditComplaint;
import com.empik.complaint_service.model.Submitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ComplaintService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ComplaintService.class);
    private final ComplaintRepository complaintRepository;
    private final SubmitterRepository submitterRepository;
    private final CountryResolver countryResolver;
    private static final int FIRST_SUBMIT_COUNT = 1;

    public ComplaintService(ComplaintRepository complaintRepository, SubmitterRepository submitterRepository,
                            CountryResolver countryResolver) {
        this.complaintRepository = complaintRepository;
        this.submitterRepository = submitterRepository;
        this.countryResolver = countryResolver;
    }

    public Complaint addComplaint(Complaint complaint, String clientIpAddress) {
        SubmitterEntity submitterEntity = getOrCreateSubmitter(complaint.getSubmitter());
        Optional<Complaint> duplicatedComplaint = updateComplaintIfDuplicated(submitterEntity, complaint);
        return duplicatedComplaint.orElseGet(() -> submitNewComplaint(complaint, submitterEntity, clientIpAddress));
    }

    public Optional<Complaint> editComplaint(EditComplaint editComplaint) {
        Optional<ComplaintEntity> existingComplaint = complaintRepository.findById(editComplaint.getComplaintId());
        if (existingComplaint.isPresent()) {
            ComplaintEntity complaintEntity = existingComplaint.get();
            complaintEntity.setDescription(editComplaint.getDescription());
            ComplaintEntity savedComplaint = complaintRepository.save(complaintEntity);
            return Optional.of(ComplaintMapper.mapToModel(savedComplaint));
        }
        LOGGER.warn("Complaint with ID {} not found for editing", editComplaint.getComplaintId());
        return Optional.empty();
    }

    public Optional<Complaint> getComplaintById(Long id) {
        Optional<ComplaintEntity> complaintEntity = complaintRepository.findById(id);
        return complaintEntity.map(ComplaintMapper::mapToModel);
    }

    public List<Complaint> getAllComplaints() {
        List<ComplaintEntity> complaintEntities = complaintRepository.findAll();
        return complaintEntities.stream()
                .map(ComplaintMapper::mapToModel)
                .toList();
    }

    private SubmitterEntity getOrCreateSubmitter(Submitter submitter) {
        Optional<SubmitterEntity> existingSubmitter = submitterRepository.findByEmailAddress(submitter.getEmailAddress());
        return existingSubmitter.orElseGet(() -> {
            SubmitterEntity newSubmitterEntity = new SubmitterEntity(submitter.getFirstName(), submitter.getLastName(),
                    submitter.getEmailAddress());
            return submitterRepository.save(newSubmitterEntity);
        });
    }

    private Optional<Complaint> updateComplaintIfDuplicated(SubmitterEntity submitterEntity, Complaint complaint) {
        Optional<ComplaintEntity> duplicatedComplaintEntity = submitterEntity.getComplaintEntities().stream()
                .filter(complaintEntity -> complaintEntity.getProductId().equals(complaint.getProductId()))
                .findFirst();
        return duplicatedComplaintEntity.map(this::updateSubmitCount);
    }

    private Complaint updateSubmitCount(ComplaintEntity complaintEntity) {
        complaintEntity.setSubmitCount(complaintEntity.getSubmitCount() + 1);
        complaintRepository.save(complaintEntity);
        return ComplaintMapper.mapToModel(complaintEntity);
    }

    private Complaint submitNewComplaint(Complaint complaint, SubmitterEntity submitter, String clientIpAddress) {
        ComplaintEntity complaintEntity = new ComplaintEntity(complaint.getProductId(), complaint.getDescription(),
                LocalDateTime.now(), submitter, countryResolver.getCountryByIp(clientIpAddress), FIRST_SUBMIT_COUNT);
        List<ComplaintEntity> complaintEntities = submitter.getComplaintEntities();
        complaintEntities.add(complaintEntity);
        submitter.setComplaintEntities(complaintEntities);
        ComplaintEntity savedComplaint = complaintRepository.save(complaintEntity);
        return ComplaintMapper.mapToModel(savedComplaint);
    }
}
