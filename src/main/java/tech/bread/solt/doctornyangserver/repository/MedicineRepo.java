package tech.bread.solt.doctornyangserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bread.solt.doctornyangserver.model.entity.Medicine;
import tech.bread.solt.doctornyangserver.model.entity.Prescription;

import java.util.List;

public interface MedicineRepo extends JpaRepository<Medicine, Integer> {
    Medicine findOneById(int id);
    List<Medicine> findAllByPrescriptionId(Prescription prescriptionId);
}
