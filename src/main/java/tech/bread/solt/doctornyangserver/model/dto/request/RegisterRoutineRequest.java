package tech.bread.solt.doctornyangserver.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRoutineRequest {
    String id;
    String name;
    int maxPerform;
    LocalDate date;
    String colorCode;
    int maxTerm;
}
