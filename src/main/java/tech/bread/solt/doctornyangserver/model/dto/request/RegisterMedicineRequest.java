package tech.bread.solt.doctornyangserver.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterMedicineRequest {
    int prescriptionId;
    String medicineName;
    int once;
    int days;
    List<Integer> time;
    int dosage;
}
