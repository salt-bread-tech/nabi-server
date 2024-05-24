package tech.bread.solt.doctornyangserver.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateIngestionRequest {
    int ingestionId;
    private LocalDate date;
    private int times;
    double servingSize;
    double totalIngestionSize;
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
