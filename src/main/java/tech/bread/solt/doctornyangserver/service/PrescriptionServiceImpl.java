package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.PostPrescriptionRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.UpdatePrescriptionRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.GetPrescriptionResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.GetPrescriptionsByDateResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.GetPrescriptionsResponse;
import tech.bread.solt.doctornyangserver.model.entity.Dosage;
import tech.bread.solt.doctornyangserver.model.entity.Medicine;
import tech.bread.solt.doctornyangserver.model.entity.Prescription;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.repository.DosageRepo;
import tech.bread.solt.doctornyangserver.repository.MedicineRepo;
import tech.bread.solt.doctornyangserver.repository.PrescriptionRepo;
import tech.bread.solt.doctornyangserver.repository.UserRepo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrescriptionServiceImpl implements PrescriptionService {
    private final UserRepo userRepo;
    private final PrescriptionRepo prescriptionRepo;
    private final MedicineRepo medicineRepo;
    private final DosageRepo dosageRepo;

    @Override
    public int addPrescription(PostPrescriptionRequest request, String id) {
        Optional<User> user = userRepo.findById(id);
        Prescription prescription;

        if (user.isPresent()) {
            prescription = Prescription.builder()
                    .userUid(user.get())
                    .name(request.getName())
                    .date(request.getDate())
                    .build();
            prescriptionRepo.save(prescription);
            return 200;
        }

        return 0;
    }

    @Override
    public List<GetPrescriptionsResponse> getPrescriptions(String id) {
        Optional<User> user = userRepo.findById(id);
        List<Prescription> prescriptions = new ArrayList<>();
        List<GetPrescriptionsResponse> responses = new ArrayList<>();

        if (user.isPresent()) {
            prescriptions = prescriptionRepo.findAllByUserUidOrderByDateDescIdDesc(user.get());

            for (Prescription p : prescriptions) {
                responses.add(new GetPrescriptionsResponse(p.getId(), p.getName(), p.getDate()));
            }

            return responses;
        }

        return responses;
    }

    @Override
    public GetPrescriptionResponse getPrescription(int prescriptionId, String id) {
        Optional<Prescription> prescription = prescriptionRepo.findAllById(prescriptionId);
        GetPrescriptionResponse response = new GetPrescriptionResponse();
        List<GetPrescriptionResponse.MedicineTaking> medicineTakings = new ArrayList<>();
        Optional<User> optionalUser = userRepo.findById(id);

        if (optionalUser.isEmpty()) {
            return response;
        }
        if (prescription.isPresent()) {
            Prescription p = prescription.get();

            List<Medicine> medicines = medicineRepo.findAllByPrescriptionId(p);

            for (Medicine m : medicines) {
                boolean registeredDosingSchedule = false;

                List<Integer> timeList = new ArrayList<>();
                if (m.isBreakfast()) {
                    timeList.add(0);
                }
                if (m.isLunch()) {
                    timeList.add(1);
                }
                if (m.isDinner()) {
                    timeList.add(2);
                }
                if (m.isBeforeSleep()) {
                    timeList.add(3);
                }

                List<Dosage> dosages = dosageRepo.findByUserUid(optionalUser.get());
                for (Dosage d : dosages) {
                    if (d.getMedicineId().getId().equals(m.getId())) {
                        registeredDosingSchedule = true;
                        break;
                    }
                }

                medicineTakings.add(GetPrescriptionResponse.MedicineTaking.builder()
                                .medicineId(m.getId())
                                .medicineName(m.getMedicineName())
                                .once(m.getOnceDosage())
                                .days(m.getTotalDosage()/m.getDailyDosage())
                                .time(timeList)
                                .dosage(m.getMedicineDosage())
                                .registeredDosingSchedule(registeredDosingSchedule)
                                .build());
            }

            response = GetPrescriptionResponse.builder()
                    .prescriptionName(p.getName())
                    .prescriptionDate(p.getDate())
                    .medicineTakings(medicineTakings)
                    .build();
        }

        return response;
    }

    @Override
    public int update(UpdatePrescriptionRequest request) {
        Optional<Prescription> optionalPrescription =
                prescriptionRepo.findAllById(request.getPrescriptionId());

        if (optionalPrescription.isPresent()) {
            Prescription updatedPrescription = optionalPrescription.get();

            // 처방전 수정
            long dayBetween = ChronoUnit.DAYS.between(updatedPrescription.getDate(), request.getDate());
            updatedPrescription.setName(request.getName());
            updatedPrescription.setDate(request.getDate());
            Prescription p = prescriptionRepo.save(updatedPrescription);

            // 수정된 처방전에 대한 의약품과 복용 일정 수정
            List<Medicine> medicines = medicineRepo.findAllByPrescriptionId(p);
            for (Medicine m : medicines) {
                List<Dosage> dosages = dosageRepo.findByMedicineId(m);
                for (Dosage d : dosages) {
                    d.setDate(d.getDate().plusDays(dayBetween));
                    dosageRepo.save(d);
                }
                log.info("복용일정 수정 성공");
            }
            log.info("처방전 수정 성공");
            return 200;
        }
        log.warn("처방전 정보를 찾지 못함");
        return 100;
    }

    @Override
    public boolean delete(int prescriptionId) {
        Optional<Prescription> optionalPrescription = prescriptionRepo.findAllById(prescriptionId);

        if (optionalPrescription.isPresent()) {
            prescriptionRepo.delete(optionalPrescription.get());
            log.info("처방전 정보 삭제 성공");
            return true;
        }
        return false;
    }

    @Override
    public List<GetPrescriptionsByDateResponse> getPrescriptionsByDate(LocalDate date, String id) {
        Optional<User> user = userRepo.findById(id);
        List<Prescription> prescriptions = new ArrayList<>();
        List<GetPrescriptionsByDateResponse> responses = new ArrayList<>();

        if (user.isPresent()) {
            prescriptions = prescriptionRepo.findTop3ByUserUidAndDateLessThanEqualOrderByDateDescIdDesc(user.get(), date);

            for (Prescription p : prescriptions) {
                responses.add(new GetPrescriptionsByDateResponse(p.getName(), p.getDate()));
            }

            return responses;
        }

        return responses;
    }
}
