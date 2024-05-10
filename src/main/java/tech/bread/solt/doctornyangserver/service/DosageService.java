package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.DeleteDosageRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.DoneDosageRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.DosageRegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.ShowDosageResponse;

import java.util.List;

public interface DosageService {
    int registerDosage(DosageRegisterRequest request);
    Boolean toggleDosage(DoneDosageRequest request);
    List<ShowDosageResponse> getMedicineDosage(String id);
    int deleteDosage(DeleteDosageRequest request);
}
