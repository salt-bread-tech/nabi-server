package tech.bread.solt.doctornyangserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bread.solt.doctornyangserver.model.entity.Routine;
import tech.bread.solt.doctornyangserver.model.entity.SetRoutine;
import tech.bread.solt.doctornyangserver.model.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SetRoutineRepo extends JpaRepository<SetRoutine, Integer> {
    List<SetRoutine> findByRoutineIdAndUserUid(Routine routineId, User userUid);
    List<SetRoutine> findByUserUidAndRoutineIdAndPerformDateAndCompletionFalse(User userUid, Routine routineId, LocalDate performDate);
    Long countByUserUidAndCompletionAndPerformDateBetween(User userUid, Boolean b, LocalDate startDate, LocalDate endDate);
}
