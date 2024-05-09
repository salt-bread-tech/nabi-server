package tech.bread.solt.doctornyangserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.bread.solt.doctornyangserver.model.entity.Chat;
import tech.bread.solt.doctornyangserver.model.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatRepo extends JpaRepository<Chat, Integer> {
    List<Chat> findTop10ByUidOrderByCreateAtDesc(User user);
    List<Chat> findTop40ByUidOrderByCreateAtDesc(User user);
    Page<Chat> findByUidAndCreateAtBefore(User user, LocalDateTime date, Pageable pageable);

}