package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.DoneDosageRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.DosageRegisterRequest;

public interface DosageService {
    int registerDosage(DosageRegisterRequest request);
    Boolean toggleDosage(DoneDosageRequest request);
}
