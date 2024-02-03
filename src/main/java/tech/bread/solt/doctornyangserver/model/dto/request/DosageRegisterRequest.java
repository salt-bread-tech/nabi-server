package tech.bread.solt.doctornyangserver.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DosageRegisterRequest {
    private int userUid;
    private int medicineId;
}
