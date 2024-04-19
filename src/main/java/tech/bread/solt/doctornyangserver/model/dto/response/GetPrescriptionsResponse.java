package tech.bread.solt.doctornyangserver.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPrescriptionsResponse {
    private int prescriptionId;
    private String name;
    private LocalDate date;
}
