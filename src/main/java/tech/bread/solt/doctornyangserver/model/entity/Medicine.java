package tech.bread.solt.doctornyangserver.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "medicine")
@Builder
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @ManyToOne
    @JoinColumn(name = "medicine_id")
    MedicineInformation medicineId;

    @ManyToOne
    @JoinColumn(name = "prescription_id")
    Prescription prescriptionId;

    @Column(name = "daily_dosage")
    Integer dailyDosage;

    @Column(name = "total_dosage")
    Integer totalDosage;

    @Column(name = "once_dosage")
    Integer onceDosage;
}