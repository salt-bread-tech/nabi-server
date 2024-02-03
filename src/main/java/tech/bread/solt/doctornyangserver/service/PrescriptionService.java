package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.PostPrescriptionRequest;

public interface PrescriptionService {
    int addPrescriptions(PostPrescriptionRequest request);
}
