package tech.bread.solt.doctornyangserver.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.bread.solt.doctornyangserver.util.Times;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetDietResponse {

    int dietId;
    int foodId;
    Times times;
    String name;
    Double servingSize;
    Double calories;
    Double carbohydrate;
    Double protein;
    Double fat;

}
