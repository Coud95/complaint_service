package com.empik.complaint_service.domain;

import com.empik.complaint_service.infrastructure.entity.ComplaintEntity;
import com.empik.complaint_service.infrastructure.entity.SubmitterEntity;
import com.empik.complaint_service.infrastructure.repository.ComplaintRepository;
import com.empik.complaint_service.infrastructure.repository.SubmitterRepository;
import com.empik.complaint_service.model.Complaint;
import com.empik.complaint_service.model.EditComplaint;
import com.empik.complaint_service.model.Submitter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComplaintServiceTest {

    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String EMAIL_ADDRESS = "john.doe@empik.pl";
    private static final long PRODUCT_ID = 1;
    private static final String COUNTRY = "US";
    private static final String DESCRIPTION = "Test complaint";
    private static final String CLIENT_IP_ADDRESS = "127.0.0.1";

    @Mock
    private ComplaintRepository complaintRepository;

    @Mock
    private SubmitterRepository submitterRepository;

    @Mock
    private CountryResolver countryResolver;

    @InjectMocks
    private ComplaintService complaintService;

    @Test
    void addComplaintCreatesNewComplaint() {
        Submitter submitter = new Submitter(FIRST_NAME, LAST_NAME, EMAIL_ADDRESS);
        Complaint complaint = new Complaint(PRODUCT_ID, DESCRIPTION, submitter);
        ComplaintEntity complaintEntity = mapToEntity(complaint, submitter);

        when(submitterRepository.findByEmailAddress(EMAIL_ADDRESS)).thenReturn(Optional.empty());
        when(submitterRepository.save(complaintEntity.getSubmitterEntity())).thenReturn(complaintEntity.getSubmitterEntity());
        when(complaintRepository.save(complaintEntity)).thenReturn(complaintEntity);
        when(countryResolver.getCountryByIp(CLIENT_IP_ADDRESS)).thenReturn(COUNTRY);

        Complaint result = complaintService.addComplaint(complaint, CLIENT_IP_ADDRESS);

        assertEntityWithModel(complaintEntity, result);
    }

    @Test
    void addComplaintUpdatesExistingComplaint() {
        Submitter submitter = new Submitter(FIRST_NAME, LAST_NAME, EMAIL_ADDRESS);
        Complaint complaint = new Complaint(PRODUCT_ID, DESCRIPTION, submitter);
        complaint.setSubmitter(submitter);
        SubmitterEntity submitterEntity = new SubmitterEntity();
        ComplaintEntity existingComplaint = mapToEntity(complaint, submitter);
        existingComplaint.setProductId(complaint.getProductId());
        existingComplaint.setDescription("Old description");
        submitterEntity.setComplaintEntities(List.of(existingComplaint));
        when(submitterRepository.findByEmailAddress(EMAIL_ADDRESS)).thenReturn(Optional.of(submitterEntity));
        when(complaintRepository.save(existingComplaint)).thenReturn(existingComplaint);

        Complaint result = complaintService.addComplaint(complaint, CLIENT_IP_ADDRESS);

        assertEntityWithModel(existingComplaint, result);
    }

    @Test
    void editComplaintEditsExistingComplaint() {
        EditComplaint editComplaint = new EditComplaint();
        editComplaint.setComplaintId(1L);
        Submitter submitter = new Submitter(FIRST_NAME, LAST_NAME, EMAIL_ADDRESS);
        Complaint complaint = new Complaint(PRODUCT_ID, DESCRIPTION, submitter);
        ComplaintEntity existingComplaint = mapToEntity(complaint, submitter);
        ComplaintEntity newComplaintEntity = mapToEntity(complaint, submitter);
        newComplaintEntity.setDescription("New description");
        when(complaintRepository.findById(1L)).thenReturn(Optional.of(existingComplaint));
        when(complaintRepository.save(newComplaintEntity)).thenReturn(newComplaintEntity);

        Complaint result = complaintService.editComplaint(editComplaint).get();

        assertEquals(newComplaintEntity.getDescription(), result.getDescription());
        assertEquals(newComplaintEntity.getComplaintId(), result.getId());
    }

    @Test
    void editComplaintComplaintNotFound() {
        EditComplaint editComplaint = new EditComplaint();
        editComplaint.setComplaintId(1L);
        when(complaintRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Complaint> result = complaintService.editComplaint(editComplaint);

        assertEquals(Optional.empty(), result);
    }

    @Test
    void getComplaintByIdReturnsComplaint() {
        SubmitterEntity submitterEntity = new SubmitterEntity(FIRST_NAME, LAST_NAME, EMAIL_ADDRESS);
        ComplaintEntity complaintEntity = new ComplaintEntity(PRODUCT_ID, DESCRIPTION, LocalDateTime.now(), submitterEntity, COUNTRY, 1);
        when(complaintRepository.findById(1L)).thenReturn(Optional.of(complaintEntity));

        Complaint result = complaintService.getComplaintById(1L).get();

        assertEntityWithModel(complaintEntity, result);
    }

    @Test
    void getComplaintByIdComplaintNotFound() {
        when(complaintRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Complaint> result = complaintService.getComplaintById(1L);

        assertEquals(Optional.empty(), result);
    }

    @Test
    void getAllComplaintsReturnsAllComplaints() {
        SubmitterEntity submitterEntity = new SubmitterEntity(FIRST_NAME, LAST_NAME, EMAIL_ADDRESS);
        ComplaintEntity complaintEntity = new ComplaintEntity(PRODUCT_ID, DESCRIPTION, LocalDateTime.now(), submitterEntity, COUNTRY, 1);
        List<ComplaintEntity> complaintEntities = List.of(complaintEntity);
        when(complaintRepository.findAll()).thenReturn(complaintEntities);

        Complaint result = complaintService.getAllComplaints().getFirst();

        assertEntityWithModel(complaintEntity, result);
    }

    private void assertEntityWithModel(ComplaintEntity complaintEntity, Complaint result) {
        assertEquals(complaintEntity.getProductId(), result.getProductId());
        assertEquals(complaintEntity.getDescription(), result.getDescription());
        SubmitterEntity submitterEntity = complaintEntity.getSubmitterEntity();
        Submitter submitter = result.getSubmitter();
        assertEquals(submitterEntity.getFirstName(), submitter.getFirstName());
        assertEquals(submitterEntity.getLastName(), submitter.getLastName());
        assertEquals(submitterEntity.getEmailAddress(), submitter.getEmailAddress());
        assertEquals(complaintEntity.getSubmitCount(), result.getSubmitCount());
        assertEquals(complaintEntity.getCreationDate().toString(), result.getCreationDate());
        assertEquals(complaintEntity.getCountry(), result.getCountry());
    }

    private ComplaintEntity mapToEntity(Complaint complaint, Submitter submitter) {
        ComplaintEntity complaintEntity = new ComplaintEntity();
        complaintEntity.setProductId(complaint.getProductId());
        complaintEntity.setDescription(complaint.getDescription());
        complaintEntity.setCreationDate(LocalDateTime.now());
        complaintEntity.setCountry(COUNTRY);
        complaintEntity.setSubmitCount(1);
        SubmitterEntity submitterEntity = new SubmitterEntity();
        submitterEntity.setFirstName(submitter.getFirstName());
        submitterEntity.setLastName(submitter.getLastName());
        submitterEntity.setEmailAddress(submitter.getEmailAddress());
        complaintEntity.setSubmitterEntity(submitterEntity);
        return complaintEntity;
    }
}