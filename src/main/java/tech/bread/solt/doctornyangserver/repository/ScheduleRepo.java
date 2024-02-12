package tech.bread.solt.doctornyangserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bread.solt.doctornyangserver.model.entity.Schedule;
import tech.bread.solt.doctornyangserver.model.entity.User;

import java.util.List;

public interface ScheduleRepo extends JpaRepository<Schedule, Integer> {
    List<Schedule> findByUserUid(User userUid);
}
