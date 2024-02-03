package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.MedicineRegisterRequest;

public interface MedicineService {
    int registerMedicine(MedicineRegisterRequest request);
}
