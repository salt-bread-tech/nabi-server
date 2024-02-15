package tech.bread.solt.doctornyangserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bread.solt.doctornyangserver.model.entity.SetRoutine;

public interface SetRoutineRepo extends JpaRepository<SetRoutine, Integer> {
}
