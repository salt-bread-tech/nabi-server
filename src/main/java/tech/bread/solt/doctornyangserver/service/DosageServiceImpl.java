package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.DoneDosageRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.DosageRegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.SetPrivateDosageRequest;
import tech.bread.solt.doctornyangserver.model.entity.Dosage;
import tech.bread.solt.doctornyangserver.model.entity.Medicine;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.repository.DosageRepo;
import tech.bread.solt.doctornyangserver.repository.MedicineRepo;
import tech.bread.solt.doctornyangserver.repository.UserRepo;
import tech.bread.solt.doctornyangserver.util.Times;

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

    @Override
    public int registerDosage(DosageRegisterRequest request) {
        Optional<Medicine> m = medicineRepo.findById(request.getMedicineId());
        Optional<User> u = userRepo.findById(request.getUserUid());

        if (m.isPresent() && u.isPresent()) {
            Medicine medicine = m.get();

            int doseDays = medicine.getTotalDosage() / medicine.getDailyDosage();
            String desc = medicine.getMedicineDosage();
            List<Integer> ordinals = new ArrayList<>();

            if (desc.contains("식후")) {
                ordinals.add(1);
                ordinals.add(3);
                ordinals.add(5);
            } else if (desc.contains("식전")) {
                ordinals.add(2);
                ordinals.add(4);
                ordinals.add(6);
            }

            if (desc.contains("취침"))
                ordinals.add(6);

            for (int i = 0; i < doseDays; i++) {
                for (int j = 0; j < medicine.getDailyDosage(); j++) {
                    dosageRepo.save(Dosage.builder()
                            .date(request.getStartDate().plusDays(i))
                            .userUid(u.get())
                            .times(Times.ofOrdinal(ordinals.get(j)))
                            .medicineId(m.get())
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
    public Boolean toggleDosage(DoneDosageRequest request) {
        User u = userRepo.findOneByUid(request.getUserUid());
        Medicine m = medicineRepo.findOneById(request.getMedicineId());

        Optional<Dosage> d = dosageRepo.findByUserUidAndMedicineIdAndTimesAndDate(
                u, m, request.getTimes(), request.getDate()
        );

        if (d.isPresent()){
            Dosage dosage = d.get();
            dosage.setMedicineTaken(!dosage.getMedicineTaken());
            dosageRepo.save(dosage);

            System.out.println("변경 완료");
            return true;
        }
        System.out.println("변경 실패");
        return false;
    }

    @Override
    public int registerPrivateDosage(SetPrivateDosageRequest request) {
        Optional<Medicine> medicine = medicineRepo.findById(request.getMedicineId());
        Optional<User> user = userRepo.findById(request.getUserUid());

        if (medicine.isPresent() && user.isPresent()){
            Medicine privateMedicine = medicine.get();
            int doseDays = request.getTotalDosage() / privateMedicine.getDailyDosage();
            int restDosage = request.getTotalDosage() % privateMedicine.getDailyDosage();
            String desc = privateMedicine.getMedicineDosage();
            List<Integer> ordinals = new ArrayList<>();

            if (desc.contains("식후")){
                ordinals.add(1);
                ordinals.add(3);
                ordinals.add(5);
            } else if (desc.contains("식전")) {
                ordinals.add(2);
                ordinals.add(4);
                ordinals.add(6);
            }
            if (desc.contains("취침"))
                ordinals.add(6);

            for (int i = 0; i < doseDays; i++) {
                for (int j = 0; j < privateMedicine.getDailyDosage(); j++) {
                    dosageRepo.save(Dosage.builder()
                            .date(request.getStartDate().plusDays(i))
                            .userUid(user.get())
                            .times(Times.ofOrdinal(ordinals.get(j)))
                            .medicineId(privateMedicine)
                            .medicineTaken(false).build());
                }
            }
            if (restDosage > 0){
                for(int i = 0; i < restDosage; i++) {
                    dosageRepo.save(Dosage.builder().
                            date(request.getStartDate().plusDays(doseDays))
                            .userUid(user.get())
                            .times(Times.ofOrdinal(ordinals.get(i)))
                            .medicineId(privateMedicine)
                            .medicineTaken(false).build());
                }
            }
            System.out.println("등록 성공");
            return 200;
        }
        System.out.println("등록 실패: 약 정보가 없음");
        return 500;
    }
}
