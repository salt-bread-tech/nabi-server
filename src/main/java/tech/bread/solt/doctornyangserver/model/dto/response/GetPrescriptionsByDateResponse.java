package tech.bread.solt.doctornyangserver.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPrescriptionsByDateResponse {
    private String name;
    private LocalDate date;
}
