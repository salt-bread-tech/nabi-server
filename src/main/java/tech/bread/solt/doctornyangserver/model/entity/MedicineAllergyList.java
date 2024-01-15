package tech.bread.solt.doctornyangserver.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "medicine_allergy_list")
@Builder
public class MedicineAllergyList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @ManyToOne
    @JoinColumn(name = "medicine_id")
    MedicineInformation medicineId;

    @ManyToOne
    @JoinColumn(name = "allergy_information_id")
    AllergyInformation allergyInformationId;
}
