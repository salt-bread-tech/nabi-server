package tech.bread.solt.doctornyangserver.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoneDosageRequest {
    private String userId;
    private int medicineId;
    private LocalDate date;
    private int times;
    private int dosages;
}
