package tech.bread.solt.doctornyangserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bread.solt.doctornyangserver.model.entity.Dosage;
import tech.bread.solt.doctornyangserver.model.entity.Medicine;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.util.Times;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DosageRepo extends JpaRepository<Dosage, Integer>{
    Optional<Dosage> findByUserUidAndMedicineIdAndTimesAndDate(User uid, Medicine medicineId, Times times, LocalDate date);

    List<Dosage> findByUserUid(User uid);
}
