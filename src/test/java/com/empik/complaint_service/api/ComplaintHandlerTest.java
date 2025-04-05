package com.empik.complaint_service.api;

import com.empik.complaint_service.domain.ComplaintService;
import com.empik.complaint_service.model.Complaint;
import com.empik.complaint_service.model.EditComplaint;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComplaintHandlerTest {

    @Mock
    private ComplaintService complaintService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private ComplaintHandler complaintHandler;

    @Test
    void addComplaintCreatesComplaint() {
        Complaint complaint = new Complaint();
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(complaintService.addComplaint(any(Complaint.class), any(String.class))).thenReturn(complaint);

        ResponseEntity<Complaint> response = complaintHandler.addComplaint(complaint);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(complaint, response.getBody());
    }

    @Test
    void editComplaintEditsExistingComplaint() {
        EditComplaint editComplaint = new EditComplaint();
        Complaint complaint = new Complaint();
        when(complaintService.editComplaint(any(EditComplaint.class))).thenReturn(Optional.of(complaint));

        ResponseEntity<Complaint> response = complaintHandler.editComplaint(editComplaint);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(complaint, response.getBody());
    }

    @Test
    void editComplaintRequestShouldReturnNotFound() {
        EditComplaint editComplaint = new EditComplaint();
        when(complaintService.editComplaint(any(EditComplaint.class))).thenReturn(Optional.empty());

        ResponseEntity<Complaint> response = complaintHandler.editComplaint(editComplaint);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getComplaintByIdReturnsComplaint() {
        Complaint complaint = new Complaint();
        when(complaintService.getComplaintById(any(Long.class))).thenReturn(Optional.of(complaint));

        ResponseEntity<Complaint> response = complaintHandler.getComplaintById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(complaint, response.getBody());
    }

    @Test
    void getComplaintByIdShouldReturnNotFound() {
        when(complaintService.getComplaintById(any(Long.class))).thenReturn(Optional.empty());

        ResponseEntity<Complaint> response = complaintHandler.getComplaintById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAllComplaintsReturnsAllComplaints() {
        List<Complaint> complaints = List.of(new Complaint(), new Complaint());
        when(complaintService.getAllComplaints()).thenReturn(complaints);

        ResponseEntity<List<Complaint>> response = complaintHandler.getAllComplaints();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(complaints, response.getBody());
    }
}