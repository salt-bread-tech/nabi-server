package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.GetMedicineRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterMedicineRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.GetMedicineDescriptionResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.GetMedicineResponse;
import tech.bread.solt.doctornyangserver.model.entity.Medicine;

import java.util.List;

public interface MedicineService {

    List<String> getMedicineList(String medicineName);

    GetMedicineDescriptionResponse getMedicineDescription(String name, int num);

    int registerMedicine(RegisterMedicineRequest request);

    List<GetMedicineResponse> getMedicineList(GetMedicineRequest request);
}
