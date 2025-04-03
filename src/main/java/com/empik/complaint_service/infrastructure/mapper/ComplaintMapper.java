package com.empik.complaint_service.infrastructure.mapper;

import com.empik.complaint_service.infrastructure.entity.ComplaintEntity;
import com.empik.complaint_service.model.Complaint;

import java.time.LocalDateTime;

public class ComplaintMapper {

    public static ComplaintEntity mapToEntity(Complaint complaint) {
        ComplaintEntity mappedComplaintEntity = new ComplaintEntity();
        mappedComplaintEntity.setComplaintId(complaint.getId());
        mappedComplaintEntity.setProductId(complaint.getProductId());
        mappedComplaintEntity.setDescription(complaint.getDescription());
        mappedComplaintEntity.setSubmitterEntity(SubmitterMapper.mapToEntity(complaint.getSubmitter()));
        mappedComplaintEntity.setCreationDate(LocalDateTime.parse(complaint.getCreationDate()));
        mappedComplaintEntity.setCountry(complaint.getCountry());
        mappedComplaintEntity.setSubmitCount(complaint.getSubmitCount());
        return mappedComplaintEntity;
    }

    public static Complaint mapToModel(ComplaintEntity complaintEntity) {
        Complaint mappedComplaint = new Complaint();
        mappedComplaint.setId(complaintEntity.getComplaintId());
        mappedComplaint.setProductId(complaintEntity.getProductId());
        mappedComplaint.setDescription(complaintEntity.getDescription());
        mappedComplaint.setSubmitter(SubmitterMapper.mapToModel(complaintEntity.getSubmitterEntity()));
        mappedComplaint.setCreationDate(complaintEntity.getCreationDate().toString());
        mappedComplaint.setCountry(complaintEntity.getCountry());
        mappedComplaint.setSubmitCount(complaintEntity.getSubmitCount());
        return mappedComplaint;
    }
}
