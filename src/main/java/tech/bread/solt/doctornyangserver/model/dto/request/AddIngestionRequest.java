package tech.bread.solt.doctornyangserver.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddIngestionRequest {
    private int uid;
    private int times;
    private String foodName;
    private double servingSize;
    private double calories;
    private double carbohydrate;
    private double protein;
    private double fat;
    private double sugars;
    private double salt;
    private double cholesterol;
    private double saturatedFattyAcid;
    private double transFattyAcid;
}
