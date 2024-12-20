package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.RegisterMedicineRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.UpdateMedicineRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.GetMedicineDescriptionResponse;

import java.util.List;

public interface MedicineService {

    List<String> getMedicineList(String medicineName);

    GetMedicineDescriptionResponse getMedicineDescription(String name, int num);

    int register(RegisterMedicineRequest request, String id);

    boolean delete(int medicineId);

    int update(UpdateMedicineRequest request);
}
