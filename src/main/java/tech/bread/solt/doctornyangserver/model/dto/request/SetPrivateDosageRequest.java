package tech.bread.solt.doctornyangserver.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetPrivateDosageRequest {
    private int userUid;
    private int medicineId;
    private LocalDate startDate;
    private int totalDosage;
}