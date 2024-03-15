package tech.bread.solt.doctornyangserver.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateIngestionRequest {
    int ingestionId;
    double servingSize;
    double calories;
    double carbohydrate;
    double protein;
    double fat;
    double sugars;
    double salt;
    double cholesterol;
    double saturatedFattyAcid;
    double transFattyAcid;
}
