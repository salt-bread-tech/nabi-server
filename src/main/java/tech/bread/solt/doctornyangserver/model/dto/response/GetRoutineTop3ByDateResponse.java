package tech.bread.solt.doctornyangserver.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetRoutineTop3ByDateResponse {
    int id;
    String name;
    int max;
    String color;
    int counts;
}
