package tech.bread.solt.doctornyangserver.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetIngestionTotalResponse {

    double totalKcal;
    double breakfastKcal;
    double lunchKcal;
    double dinnerKcal;
    double snackKcal;
    double totalCarbohydrate;
    double totalProtein;
    double totalFat;

}
