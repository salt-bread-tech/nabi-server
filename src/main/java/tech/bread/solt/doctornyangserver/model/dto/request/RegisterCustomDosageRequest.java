package tech.bread.solt.doctornyangserver.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCustomDosageRequest {
    String userId;
    int medicineId;
    LocalDate date;
    int time;
    int dosage;
}
