package tech.bread.solt.doctornyangserver.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterMedicineRequest {
    int uid;
    LocalDate startDate;
    String medicineName;
    int once;
    int total;
    int daily;
    String dosage;
}
