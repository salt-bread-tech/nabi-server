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

    @Column(name = "medicine_name")
    String medicineName;

    @ManyToOne
    @JoinColumn(name = "prescription_id")
    Prescription prescriptionId;

    @Column(name = "medicine_dosage")
    String medicineDosage;

    @Column(name = "daily_dosage")
    Integer dailyDosage;

    @Column(name = "total_dosage")
    Integer totalDosage;

    @Column(name = "once_dosage")
    Integer onceDosage;

    @Column(name = "medicine_dosage")
    String medicineDosage;
}
