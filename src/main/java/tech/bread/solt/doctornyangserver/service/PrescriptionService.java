package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.PostPrescriptionRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.UpdatePrescriptionRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.GetPrescriptionResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.GetPrescriptionsByDateResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.GetPrescriptionsResponse;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface PrescriptionService {
    int addPrescription(PostPrescriptionRequest request, String id);

    List<GetPrescriptionsResponse> getPrescriptions(String id);

    GetPrescriptionResponse getPrescription(int prescriptionId, String id);

    int update(UpdatePrescriptionRequest request);
    boolean delete(int prescriptionId);

    List<GetPrescriptionsByDateResponse> getPrescriptionsByDate(LocalDate date, String id);
}
