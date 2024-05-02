package tech.bread.solt.doctornyangserver.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "routine")
@Builder
public class Routine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "routine_id")
    Integer routineId;

    @ManyToOne
    @JoinColumn(name = "user_uid")
    User userUid;

    @Column(name = "routine_name")
    String routineName;

    @Column(name = "max_perform")
    Integer maxPerform;

    @Column(name = "start_date")
    LocalDate startDate;

    @Column(name = "color_code")
    String colorCode;

    @Column(name = "perform_counts")
    Integer performCounts;
}
