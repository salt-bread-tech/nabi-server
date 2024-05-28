package tech.bread.solt.doctornyangserver.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.bread.solt.doctornyangserver.util.IngestionTimes;
import tech.bread.solt.doctornyangserver.util.IngestionTimesConverter;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ingestion")
@Builder
public class Ingestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "times")
    @Convert(converter = IngestionTimesConverter.class)
    IngestionTimes ingestionTimes;

    @ManyToOne
    @JoinColumn(name = "user_uid")
    User userUid;

    @ManyToOne
    @JoinColumn(name = "food_id")
    FoodInformation foodId;

    @Column(name = "date")
    LocalDate date;
}
