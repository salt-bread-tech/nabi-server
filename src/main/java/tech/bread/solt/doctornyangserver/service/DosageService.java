package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.DoneDosageRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.DosageRegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.UpdateDosageRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.ShowDosageResponse;

import java.util.List;

public interface DosageService {
    int register(DosageRegisterRequest request);
    Boolean take(DoneDosageRequest request);
    List<ShowDosageResponse> getDosages(String id);
    boolean delete(int dosageId);
    int update(UpdateDosageRequest request);
}
