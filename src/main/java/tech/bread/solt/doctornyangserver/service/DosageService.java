package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.DoneDosageRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.DosageRegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.SetPrivateDosageRequest;
import tech.bread.solt.doctornyangserver.model.entity.Dosage;

import java.util.List;

public interface DosageService {
    int registerDosage(DosageRegisterRequest request);
    Boolean toggleDosage(DoneDosageRequest request);
    int registerPrivateDosage(SetPrivateDosageRequest request);
    List<Dosage> getMedicineDosage(int uid);
}
