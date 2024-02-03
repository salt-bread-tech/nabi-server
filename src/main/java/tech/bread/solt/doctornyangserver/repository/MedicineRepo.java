package tech.bread.solt.doctornyangserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bread.solt.doctornyangserver.model.entity.Medicine;

public interface MedicineRepo extends JpaRepository<Medicine, Integer> {
    Medicine findOneById(int id);
}
