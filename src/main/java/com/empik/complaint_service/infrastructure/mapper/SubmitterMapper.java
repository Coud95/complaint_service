package com.empik.complaint_service.infrastructure.mapper;

import com.empik.complaint_service.infrastructure.entity.SubmitterEntity;
import com.empik.complaint_service.model.Submitter;

public class SubmitterMapper {

    public static SubmitterEntity mapToEntity(Submitter submitter) {
        SubmitterEntity mappedSubmitter = new SubmitterEntity();
        mappedSubmitter.setFirstName(submitter.getFirstName());
        mappedSubmitter.setLastName(submitter.getLastName());
        mappedSubmitter.setEmailAddress(submitter.getEmailAddress());
        return mappedSubmitter;
    }

    public static Submitter mapToModel(SubmitterEntity submitterEntity) {
        Submitter mappedSubmitter = new Submitter();
        mappedSubmitter.setFirstName(submitterEntity.getFirstName());
        mappedSubmitter.setLastName(submitterEntity.getLastName());
        mappedSubmitter.setEmailAddress(submitterEntity.getEmailAddress());
        return mappedSubmitter;
    }
}
