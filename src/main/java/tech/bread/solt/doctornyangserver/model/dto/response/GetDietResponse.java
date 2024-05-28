package tech.bread.solt.doctornyangserver.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.bread.solt.doctornyangserver.util.DosageTimes;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetDietResponse {

    int ingestionId;
    int foodId;
    int ingestionTimes;
    String name;
    Double servingSize;
    Double totalIngestionSize;
    Double calories;
    Double carbohydrate;
    Double protein;
    Double fat;
    Double sugars;
    Double salt;
    Double cholesterol;
    Double saturatedFattyAcid;
    Double transFattyAcid;

}
