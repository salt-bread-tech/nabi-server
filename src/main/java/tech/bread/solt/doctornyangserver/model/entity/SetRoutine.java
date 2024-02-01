package tech.bread.solt.doctornyangserver.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "set_routine")
@Builder
public class SetRoutine {
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

    @Column(name = "perform")
    Integer perform;

    @Column(name = "end_date")
    Date endDate;

    @Column(name = "max_perform")
    Integer maxPerform;

    @Column(name = "completion")
    Boolean completion;
}
