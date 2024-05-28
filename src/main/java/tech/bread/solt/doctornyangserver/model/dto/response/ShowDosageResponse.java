package tech.bread.solt.doctornyangserver.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowDosageResponse {
    int dosageId;
    LocalDate date;
    int medicineId;
    String medicineName;
    int times;
    int dosage;
    boolean medicineTaken;

}
