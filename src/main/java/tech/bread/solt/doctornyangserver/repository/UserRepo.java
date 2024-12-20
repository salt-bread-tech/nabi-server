package tech.bread.solt.doctornyangserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bread.solt.doctornyangserver.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findById(String id);

    User findOneByUid(int uid);
    List<User> findByFedIsTrue();

    boolean existsById(String userId);
}
