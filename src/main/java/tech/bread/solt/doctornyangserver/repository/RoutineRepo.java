package tech.bread.solt.doctornyangserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bread.solt.doctornyangserver.model.entity.Routine;
import tech.bread.solt.doctornyangserver.model.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface RoutineRepo extends JpaRepository<Routine, Integer> {
    Routine findOneByRoutineId(int routineId);

    List<Routine> findByUserUid(User uid);

//    Long countByUserUidAndPerformCountsEqualsAndDateBetween(User user, int maxPerform, LocalDate startDate, LocalDate endDate);
    void deleteById(int routineId);
}
