package tech.bread.solt.doctornyangserver.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "food_information")
@Builder
public class FoodInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    Integer foodId;

    @Column(name="name")
    String name;

    @Column(name="serving_size")
    Double servingSize;

    @Column(name = "calories")
    Double calories;

    @Column(name = "protein")
    Double protein;

    @Column(name = "fat")
    Double fat;

    @Column(name = "carbohydrate")
    Double carbohydrate;

    @Column(name = "sugars")
    Double sugars;

    @Column(name = "salt")
    Double salt;

    @Column(name = "cholesterol")
    Double cholesterol;

    @Column(name = "saturated_fatty_acid")
    Double saturatedFattyAcid;

    @Column(name = "trans_fatty_acid")
    Double transFattyAcid;
}
