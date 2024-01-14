package tech.bread.solt.doctornyangserver.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "weight_management_tip")
@Builder
public class WeightManagementTip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "tip")
    String tip;

    @ManyToOne
    @JoinColumn(name = "bmi_range_id")
    BMIRange bmiRangeId;
}
