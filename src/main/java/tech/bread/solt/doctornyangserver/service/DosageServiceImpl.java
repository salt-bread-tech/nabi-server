package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.DoneDosageRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.DosageRegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.ShowDosageResponse;
import tech.bread.solt.doctornyangserver.model.entity.Dosage;
import tech.bread.solt.doctornyangserver.model.entity.Medicine;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.repository.DosageRepo;
import tech.bread.solt.doctornyangserver.repository.MedicineRepo;
import tech.bread.solt.doctornyangserver.repository.PrescriptionRepo;
import tech.bread.solt.doctornyangserver.repository.UserRepo;
import tech.bread.solt.doctornyangserver.util.Times;
import tech.bread.solt.doctornyangserver.util.TimesConverter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
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

            if (medicine.isBreakfast()) {
                switch (medicine.getMedicineDosage()) {
                    case "식전" -> ordinals.add(0);
                    case "식중" -> ordinals.add(1);
                    case "식후" -> ordinals.add(2);
                }
            }
            if (medicine.isLunch()) {
                switch (medicine.getMedicineDosage()) {
                    case "식전" -> ordinals.add(3);
                    case "식중" -> ordinals.add(4);
                    case "식후" -> ordinals.add(5);
                }
            }
            if (medicine.isDinner()) {
                switch (medicine.getMedicineDosage()) {
                    case "식전" -> ordinals.add(6);
                    case "식중" -> ordinals.add(7);
                    case "식후" -> ordinals.add(8);
                }
            }
            if (medicine.isBeforeSleep()) {
                ordinals.add(9);
            }

            LocalDate startDate = prescriptionRepo
                    .getPrescriptionById(medicine.getPrescriptionId().getId())
                    .get().getDate();

            for (int i = 0; i < doseDays; i++) {
                for (int j = 0; j < medicine.getDailyDosage(); j++) {
                    dosageRepo.save(Dosage.builder()
                            .date(startDate.plusDays(i))
                            .userUid(u.get())
                            .times(Times.ofOrdinal(ordinals.get(j)))
                            .medicineId(medicine)
                            .medicineTaken(false).build());
                }
            }
            System.out.println("복용 날짜 등록 완료");
            return 200;
        }
        System.out.println("복용 날짜 등록 실패");
        return 500;
    }

    @Override
    public Boolean take(DoneDosageRequest request) {
        Optional<User> u = userRepo.findById(request.getUserId());
        Medicine m = medicineRepo.findOneById(request.getMedicineId());
        Times time = new TimesConverter().convertToEntityAttribute(request.getTimes());

        if (u.isPresent()) {
            Optional<Dosage> d = dosageRepo.findByUserUidAndMedicineIdAndTimesAndDate(
                    u.get(), m, time, request.getDate()
            );
            if (d.isPresent()) {
                Dosage dosage = d.get();
                dosage.setMedicineTaken(!dosage.getMedicineTaken());
                dosageRepo.save(dosage);

                System.out.println("변경 완료");
                return true;
            }
            System.out.println("약 정보를 찾을 수 없습니다.");
            return false;
        }
        System.out.println("유저 정보를 찾을 수 없습니다.");
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
                for (Dosage dosage : d) {
                    Medicine m = medicineRepo.findOneById(dosage.getMedicineId().getId());
                    responses.add(ShowDosageResponse.builder()
                            .dosageId(dosage.getId())
                            .date(dosage.getDate())
                            .medicineId(m.getId())
                            .medicineName(m.getMedicineName())
                            .times(dosage.getTimes().getDesc())
                            .medicineTaken(dosage.getMedicineTaken()).build());
                }
                System.out.println("복용 일정 가져오기 성공");
                return responses;
            }
        }
        System.out.println("찾고자 하는 User 없음");
        return null;
    }

    @Override
    public boolean delete(int dosageId) {
        try {
            dosageRepo.deleteById(dosageId);
            System.out.println("복용 일정 삭제");
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
