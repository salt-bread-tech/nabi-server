package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.DoneDosageRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.DosageRegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterCustomDosageRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.UpdateDosageRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.ShowDosageResponse;
import tech.bread.solt.doctornyangserver.model.entity.Dosage;
import tech.bread.solt.doctornyangserver.model.entity.Medicine;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.repository.DosageRepo;
import tech.bread.solt.doctornyangserver.repository.MedicineRepo;
import tech.bread.solt.doctornyangserver.repository.PrescriptionRepo;
import tech.bread.solt.doctornyangserver.repository.UserRepo;
import tech.bread.solt.doctornyangserver.util.TakingDosages;
import tech.bread.solt.doctornyangserver.util.TakingDosagesConverter;
import tech.bread.solt.doctornyangserver.util.DosageTimes;
import tech.bread.solt.doctornyangserver.util.DosageTimesConverter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DosageServiceImpl implements DosageService {
    private final DosageRepo dosageRepo;
    private final MedicineRepo medicineRepo;
    private final UserRepo userRepo;
    private final PrescriptionRepo prescriptionRepo;

    @Override
    public int register(DosageRegisterRequest request) {
        Optional<Medicine> optionalMedicine = medicineRepo.findById(request.getMedicineId());
        Optional<User> u = userRepo.findById(request.getUserId());
        List<Integer> ordinals = new ArrayList<>();

        if (optionalMedicine.isPresent() && u.isPresent()) {
            Medicine medicine = optionalMedicine.get();

            // 총 복용일수 계산
            int doseDays = medicine.getTotalDosage() / medicine.getDailyDosage();
            if (medicine.getTotalDosage() % medicine.getDailyDosage() > 0)
                doseDays += 1;

            int medicineTaking;
            switch (medicine.getMedicineDosage()) {
                case "식전" -> medicineTaking = 0;
                case "식중" -> medicineTaking = 1;
                case "식후" -> medicineTaking = 2;
                case "상관 없음" -> medicineTaking = 3;
                default -> {
                    log.error("Illegal Arguments");
                    return 100;
                }
            }

            if (medicine.isBreakfast()) ordinals.add(0);
            if (medicine.isLunch()) ordinals.add(1);
            if (medicine.isDinner()) ordinals.add(2);
            if (medicine.isBeforeSleep()) ordinals.add(3);

            LocalDate startDate = prescriptionRepo
                    .findAllById(medicine.getPrescriptionId().getId())
                    .get().getDate();

            for (int i = 0; i < doseDays; i++) {
                for (int j = 0; j < (medicine.getDailyDosage() / medicine.getOnceDosage()); j++) {
                    dosageRepo.save(Dosage.builder()
                            .date(startDate.plusDays(i))
                            .userUid(u.get())
                            .times(DosageTimes.ofOrdinal(ordinals.get(j)))
                            .dosages(TakingDosages.ofOrdinal(medicineTaking))
                            .medicineId(medicine)
                            .medicineTaken(false).build());
                }
            }
            log.info("복용 일정 등록 성공");
            return 200;
        }
        log.warn("복용 일정 등록 실패");
        return 500;
    }

    @Override
    public Boolean take(DoneDosageRequest request) {
        Optional<User> u = userRepo.findById(request.getUserId());
        Medicine m = medicineRepo.findOneById(request.getMedicineId());
        DosageTimes time = new DosageTimesConverter().convertToEntityAttribute(request.getTimes());
        TakingDosages takingDosages =
                new TakingDosagesConverter().convertToEntityAttribute(request.getDosages());

        if (u.isPresent()) {
            Optional<Dosage> d =
                    dosageRepo.findByUserUidAndMedicineIdAndTimesAndDosagesAndDate(
                            u.get(), m, time, takingDosages, request.getDate()
            );
            if (d.isPresent()) {
                Dosage dosage = d.get();
                dosage.setMedicineTaken(!dosage.getMedicineTaken());
                dosageRepo.save(dosage);

                log.info("복용 일정 토글링");
                return true;
            }
            log.warn("약 정보를 찾을 수 없습니다.");
            return false;
        }
        log.warn("유저 정보를 찾을 수 없습니다.");
        return false;
    }

    @Override
    public List<ShowDosageResponse> getDosages(String id) {
        Optional<User> u = userRepo.findById(id);
        List<ShowDosageResponse> responses = new ArrayList<>();

        if (u.isPresent()) {
            List<Dosage> d = dosageRepo.findByUserUid(u.get());
            if (d.isEmpty()) {
                System.out.println("등록된 의약품 복용 일정이 없습니다.");
                return null;
            }
            else{
                for (tech.bread.solt.doctornyangserver.model.entity.Dosage dosage : d) {
                    Medicine m = medicineRepo.findOneById(dosage.getMedicineId().getId());
                    responses.add(ShowDosageResponse.builder()
                            .dosageId(dosage.getId())
                            .date(dosage.getDate())
                            .medicineId(m.getId())
                            .medicineName(m.getMedicineName())
                            .times(dosage.getTimes().ordinal())
                            .dosage(dosage.getDosages().ordinal())
                            .medicineTaken(dosage.getMedicineTaken()).build());
                }
                log.info("복용 일정 가져오기 성공");
                return responses;
            }
        }
        log.warn("찾고자 하는 User 없음");
        return null;
    }

    @Override
    public boolean delete(int dosageId) {
        Optional<Dosage> dosageForDelete = dosageRepo.findById(dosageId);
        if (dosageForDelete.isPresent()){
            dosageRepo.deleteById(dosageId);
            log.info("복용 일정 삭제");
            return true;
        } else {
            log.warn("찾고자 하는 복용 일정이 없음");
            return false;
        }
    }

    @Override
    public int update(UpdateDosageRequest request) {
        Optional<Dosage> optionalDosage = dosageRepo.findById(request.getDosageId());
        TakingDosages taking = new TakingDosagesConverter().convertToEntityAttribute(request.getDosages());
        DosageTimes time = new DosageTimesConverter().convertToEntityAttribute(request.getTimes());

        if (optionalDosage.isPresent()) {
            tech.bread.solt.doctornyangserver.model.entity.Dosage updateDosage = optionalDosage.get();
            updateDosage.setDate(request.getDate());
            updateDosage.setTimes(time);
            updateDosage.setDosages(taking);
            updateDosage.setMedicineTaken(false);
            dosageRepo.save(updateDosage);
            log.info("복용 일정 수정 성공");
            return 200;
        }
        log.warn("복용 일정을 찾을 수 없음");
        return 100;
    }

    @Override
    public int customize(RegisterCustomDosageRequest request) {
        Optional<Medicine> optionalMedicine = medicineRepo.findById(request.getMedicineId());
        Optional<User> optionalUser = userRepo.findById(request.getUserId());
        TakingDosages taking = new TakingDosagesConverter().convertToEntityAttribute(request.getDosage());
        DosageTimes time = new DosageTimesConverter().convertToEntityAttribute(request.getTime());

        if (optionalMedicine.isPresent() && optionalUser.isPresent()) {
            Medicine medicine = optionalMedicine.get();
            User user = optionalUser.get();

            dosageRepo.save(tech.bread.solt.doctornyangserver.model.entity.Dosage.builder()
                    .date(request.getDate())
                    .userUid(user)
                    .times(time)
                    .dosages(taking)
                    .medicineId(medicine)
                    .medicineTaken(false).build());

            log.info("등록 성공");
            return 200;
        }
        log.warn("등록 되지 않은 의약품");
        return 100;
    }

    @Override
    public boolean deleteByMedicineId(int medicineId) {
        Optional<Medicine> optionalMedicine = medicineRepo.findById(medicineId);
        if (optionalMedicine.isPresent()) {
            List<Dosage> dosages = dosageRepo.findByMedicineId(optionalMedicine.get());

            if (dosages.isEmpty()) {
                log.warn("등록된 복용 일정이 없습니다.");
                return false;
            }
            dosageRepo.deleteAll(dosages);
            log.info("복용 일정 삭제 성공");
            return true;
        }
        log.warn("해당 의약품이 존재하지 않음");
        return false;
    }
}
