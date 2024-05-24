package tech.bread.solt.doctornyangserver.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMedicineRequest {
    String userId;
    int medicineId;
    String medicineName;
    int once;
    int days;
    List<Integer> time;
    int dosage;
}
