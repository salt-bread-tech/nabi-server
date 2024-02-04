package tech.bread.solt.doctornyangserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bread.solt.doctornyangserver.model.entity.Prescription;
import tech.bread.solt.doctornyangserver.model.entity.User;

import java.util.List;

public interface PrescriptionRepo extends JpaRepository<Prescription, Integer> {
    List<Prescription> getPrescriptionsByUserUid(User userUid);
}
