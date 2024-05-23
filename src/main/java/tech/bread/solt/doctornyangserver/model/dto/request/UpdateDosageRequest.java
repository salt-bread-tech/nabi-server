package tech.bread.solt.doctornyangserver.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDosageRequest {
    String userId;
    int dosageId;
    LocalDate date;
    int times;
    int dosage;
}
