package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.DeleteDosageRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.DoneDosageRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.DosageRegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.SetPrivateDosageRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.ShowDosageResponse;
import tech.bread.solt.doctornyangserver.model.entity.Dosage;
import tech.bread.solt.doctornyangserver.model.entity.Medicine;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.repository.DosageRepo;
import tech.bread.solt.doctornyangserver.repository.MedicineRepo;
import tech.bread.solt.doctornyangserver.repository.UserRepo;
import tech.bread.solt.doctornyangserver.util.Times;
import tech.bread.solt.doctornyangserver.util.TimesConverter;

import java.sql.Time;
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
        List<Integer> ordinals = new ArrayList<>();

        if (m.isPresent() && u.isPresent()) {
            Medicine medicine = m.get();

            int doseDays = medicine.getTotalDosage() / medicine.getDailyDosage();
            if (medicine.getTotalDosage() % medicine.getDailyDosage() > 0)
                doseDays += 1;
            String desc = medicine.getMedicineDosage();

            if (desc.contains("식후")) {
                switch (medicine.getOnceDosage()) {
                    case 1:
                        switch (medicine.getDailyDosage()) {
                            case 3:
                                ordinals.add(1);
                                ordinals.add(3);
                                ordinals.add(5);
                                break;
                            case 2:
                                ordinals.add(1);
                                ordinals.add(3);
                                break;
                            case 1:
                                ordinals.add(1);
                                break;
                            default:
                                System.out.println("연산 오류");
                                break;
                        }
                        break;
                    case 2:
                        switch (medicine.getDailyDosage()){
                            case 6:
                                ordinals.add(1);
                                ordinals.add(3);
                                ordinals.add(5);
                                break;
                            case 4:
                                ordinals.add(1);
                                ordinals.add(3);
                                break;
                            case 2:
                                ordinals.add(1);
                                break;
                            default:
                                System.out.println("연산 오류");
                                break;
                        }
                        break;
                    default:
                        System.out.println("연산 오류");
                        break;
                }

            } else if (desc.contains("식전")) {
                switch (medicine.getOnceDosage()) {
                    case 1:
                        switch (medicine.getDailyDosage()) {
                            case 3:
                                ordinals.add(0);
                                ordinals.add(2);
                                ordinals.add(4);
                                break;
                            case 2:
                                ordinals.add(0);
                                ordinals.add(4);
                                break;
                            case 1:
                                ordinals.add(0);
                                break;
                            default:
                                System.out.println("연산 오류");
                                break;
                        }
                        break;
                    case 2:
                        switch (medicine.getDailyDosage()){
                            case 6:
                                ordinals.add(0);
                                ordinals.add(2);
                                ordinals.add(4);
                                break;
                            case 4:
                                ordinals.add(0);
                                ordinals.add(2);
                                break;
                            case 2:
                                ordinals.add(0);
                                break;
                            default:
                                System.out.println("연산 오류");
                                break;
                        }
                        break;
                    default:
                        System.out.println("연산 오류");
                        break;
                }
            }

            if (desc.contains("취침"))
                ordinals.add(6);

            int count = 0;
            for (int i = 0; i < doseDays; i++) {
                for (int j = 0; j < (medicine.getDailyDosage() / medicine.getOnceDosage()); j++) {
                    count++;
                    dosageRepo.save(Dosage.builder()
                            .date(request.getStartDate().plusDays(i))
                            .userUid(u.get())
                            .times(Times.ofOrdinal(ordinals.get(j)))
                            .medicineId(m.get())
                            .medicineTaken(false).build());
                    if (count >= (medicine.getTotalDosage() / medicine.getOnceDosage()))
                        break;
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
        Times time = new TimesConverter().convertToEntityAttribute(request.getTimes());

        Optional<Dosage> d = dosageRepo.findByUserUidAndMedicineIdAndTimesAndDate(
                u, m, time, request.getDate()
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

    @Override
    public List<ShowDosageResponse> getMedicineDosage(int uid) {
        Optional<User> u = userRepo.findById(uid);
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
    public int deleteDosage(DeleteDosageRequest request) {
        Optional<User> u = userRepo.findById(request.getUserUid());
        if (u.isPresent()){
            Times t = Times.ofOrdinal(request.getTimes());
            Optional<Dosage> d = dosageRepo.findByUserUidAndDateAndTimes(u.get(),
                    request.getDate(), t);
            if (d.isPresent()) {
                System.out.println("복용 일정 삭제");
                dosageRepo.delete(d.get());
                return 200;
            }
            System.out.println("찾는 복용 일정이 없습니다.");
            return 400;
        }
        System.out.println("사용자 정보가 없습니다.");
        return 500;
    }
}
