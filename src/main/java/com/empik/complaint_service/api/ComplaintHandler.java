package com.empik.complaint_service.api;

import com.empik.complaint_service.domain.ComplaintService;
import com.empik.complaint_service.model.Complaint;
import com.empik.complaint_service.model.EditComplaint;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.Optional;

@Service
public class ComplaintHandler implements ComplaintApiDelegate {

    private final ComplaintService complaintService;
    private final HttpServletRequest request;

    public ComplaintHandler(ComplaintService complaintService, HttpServletRequest request) {
        this.complaintService = complaintService;
        this.request = request;
    }

    @Override
    public ResponseEntity<Complaint> addComplaint(Complaint complaint) {
            Complaint savedComplaint = complaintService.addComplaint(complaint, request.getRemoteAddr());
            return new ResponseEntity<>(savedComplaint, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Complaint> editComplaint(EditComplaint editComplaint) {
        Optional<Complaint> editedComplaint = complaintService.editComplaint(editComplaint);
        return editedComplaint.map(complaint -> new ResponseEntity<>(complaint, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<Complaint> getComplaintById(Long id) {
        Optional<Complaint> existingComplaint = complaintService.getComplaintById(id);
        return existingComplaint.map(complaint -> new ResponseEntity<>(complaint, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<List<Complaint>> getAllComplaints() {
        return new ResponseEntity<>(complaintService.getAllComplaints(), HttpStatus.OK);
    }
}
