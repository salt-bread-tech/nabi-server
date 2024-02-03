package tech.bread.solt.doctornyangserver.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.bread.solt.doctornyangserver.util.Times;
import tech.bread.solt.doctornyangserver.util.TimesConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "dosage")
@Builder
public class Dosage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "date")
    LocalDate date;

    @Column(name = "times")
    @Convert(converter = TimesConverter.class)
    Times times;

    @Column(name = "medicine_taken")
    Boolean medicineTaken;

    @ManyToOne
    @JoinColumn(name = "user_uid")
    User userUid;

    @ManyToOne
    @JoinColumn(name = "medicine_id")
    Medicine medicineId;
}
