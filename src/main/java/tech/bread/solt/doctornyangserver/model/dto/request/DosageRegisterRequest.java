package tech.bread.solt.doctornyangserver.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DosageRegisterRequest {
    private String userId;
    private int medicineId;
    private LocalDate startDate;
}
