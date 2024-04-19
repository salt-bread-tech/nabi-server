package tech.bread.solt.doctornyangserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bread.solt.doctornyangserver.model.entity.Routine;
import tech.bread.solt.doctornyangserver.model.entity.User;

import java.util.List;

public interface RoutineRepo extends JpaRepository<Routine, Integer> {
    Routine findOneByRoutineId(int routineId);

    List<Routine> findByUId(User uid);
}
