package com.empik.complaint_service.infrastructure.mapper;

import com.empik.complaint_service.infrastructure.entity.ComplaintEntity;
import com.empik.complaint_service.model.Complaint;

public class ComplaintMapper {

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
