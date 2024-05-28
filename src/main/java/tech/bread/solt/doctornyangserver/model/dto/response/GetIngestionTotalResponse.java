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

    private double totalKcal;
    private double breakfastKcal;
    private double lunchKcal;
    private double dinnerKcal;
    private double snackKcal;
    private double totalCarbohydrate;
    private double totalProtein;
    private double totalFat;

}
