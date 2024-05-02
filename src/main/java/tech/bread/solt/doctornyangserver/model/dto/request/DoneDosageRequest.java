package tech.bread.solt.doctornyangserver.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.bread.solt.doctornyangserver.util.Times;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoneDosageRequest {
    private int userUid;
    private int medicineId;
    private LocalDate date;
    private int times;
}
