package tech.bread.solt.doctornyangserver.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowRoutineResponse {
    int id;
    String name;
    int max;
    String color;
    int counts;
}
