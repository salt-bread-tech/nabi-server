package tech.bread.solt.doctornyangserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bread.solt.doctornyangserver.model.entity.Ingestion;
import tech.bread.solt.doctornyangserver.model.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface IngestionRepo extends JpaRepository<Ingestion, Integer> {
    List<Ingestion> findAllByUserUidAndDate(User userUid, LocalDate date);
}
