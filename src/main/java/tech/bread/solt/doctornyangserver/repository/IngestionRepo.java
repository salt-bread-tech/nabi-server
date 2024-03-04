package tech.bread.solt.doctornyangserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bread.solt.doctornyangserver.model.entity.Ingestion;

public interface IngestionRepo extends JpaRepository<Ingestion, Integer> {
}
