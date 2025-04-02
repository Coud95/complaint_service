package com.empik.complaint_service.api;

import com.empik.complaint_service.domain.ComplaintService;
import com.empik.complaint_service.model.Complaint;
import com.empik.complaint_service.model.EditComplaint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ComplaintHandler implements ComplaintApiDelegate {

    private final ComplaintService complaintService;

    public ComplaintHandler(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @Override
    public ResponseEntity<Complaint> addComplaint(Complaint complaint) {
        Complaint savedComplaint = complaintService.addComplaint(complaint);
        return new ResponseEntity<>(savedComplaint, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Complaint> editComplaint(EditComplaint editComplaint) {
        Optional<Complaint> editedComplaint = complaintService.editComplaint(editComplaint);
        return editedComplaint.map(complaint -> new ResponseEntity<>(complaint, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<Complaint> showComplaintById(Long id) {
        Optional<Complaint> complaint = complaintService.getComplaintById(id);
        return complaint.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
