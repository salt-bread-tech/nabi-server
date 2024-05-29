package tech.bread.solt.doctornyangserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.model.entity.Widget;

import java.util.Optional;

public interface WidgetRepo extends JpaRepository<Widget, Integer> {
    Optional<Widget> findByUserUid(User userUid);
}
