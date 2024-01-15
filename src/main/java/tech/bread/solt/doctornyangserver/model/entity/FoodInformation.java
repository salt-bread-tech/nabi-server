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

    @Column(name = "calories")
    Double calories;

    @Column(name = "protein")
    Double protein;

    @Column(name = "fat")
    Double fat;

    @Column(name = "carbohydrate")
    Double carbohydrate;
}
