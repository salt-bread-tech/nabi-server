package tech.bread.solt.doctornyangserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.bread.solt.doctornyangserver.model.entity.Dosage;
import tech.bread.solt.doctornyangserver.model.entity.Medicine;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.util.TakingDosages;
import tech.bread.solt.doctornyangserver.util.DosageTimes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DosageRepo extends JpaRepository<Dosage, Integer>{
    Optional<Dosage> findByUserUidAndMedicineIdAndTimesAndDosagesAndDate(
            User uid, Medicine medicineId, DosageTimes dosageTimes, TakingDosages takings, LocalDate date);

    List<Dosage> findByUserUid(User uid);
    void deleteById(int dosageId);

    List<Dosage> findByMedicineId(Medicine medicine);
    List<Dosage> findByUserUidAndDate(User userUid, LocalDate date);
}
