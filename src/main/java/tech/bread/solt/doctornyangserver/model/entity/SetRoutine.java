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
@Entity(name = "set_routine")
@Builder
public class SetRoutine {   // 1일 1회를 표현, 1일 3회 루틴을 표현하려면 3개의 Entitiy 필요
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @ManyToOne
    @JoinColumn(name = "user_uid")
    User userUid;

    @ManyToOne
    @JoinColumn(name = "routine_id")
    Routine routineId;

    @Column(name = "start_date")
    LocalDate startDate;

    @Column(name = "end_date")
    LocalDate endDate;

    @Column(name = "perform_date")
    LocalDate performDate;

    @Column(name = "perform")
    Integer perform;

    @Column(name = "max_perform")
    Integer maxPerform;

    @Column(name = "completion")
    Boolean completion;
}
