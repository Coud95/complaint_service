package com.empik.complaint_service.api;

import com.empik.complaint_service.model.Complaint;
import com.empik.complaint_service.model.EditComplaint;
import com.empik.complaint_service.model.Submitter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ComplaintHandler implements ComplaintApiDelegate {

    @Override
    public ResponseEntity<Complaint> addComplaint(Complaint complaint) {
        return ComplaintApiDelegate.super.addComplaint(complaint);
    }

    @Override
    public ResponseEntity<EditComplaint> editComplaint(EditComplaint editComplaint) {
        return ComplaintApiDelegate.super.editComplaint(editComplaint);
    }

    @Override
    public ResponseEntity<Complaint> showComplaintById(Long id) {
        Complaint complaint = new Complaint();
        complaint.setCountry("Poland");
        Submitter submitter = new Submitter();
        submitter.setFirstName("Przemek");
        complaint.setSubmitter(submitter);
        return new ResponseEntity<>(complaint, HttpStatus.OK);


    }
}
