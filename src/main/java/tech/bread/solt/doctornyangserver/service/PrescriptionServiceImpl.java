package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.PostPrescriptionRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.GetPrescriptionResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.GetPrescriptionsResponse;
import tech.bread.solt.doctornyangserver.model.entity.Dosage;
import tech.bread.solt.doctornyangserver.model.entity.Medicine;
import tech.bread.solt.doctornyangserver.model.entity.Prescription;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.repository.DosageRepo;
import tech.bread.solt.doctornyangserver.repository.MedicineRepo;
import tech.bread.solt.doctornyangserver.repository.PrescriptionRepo;
import tech.bread.solt.doctornyangserver.repository.UserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
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
            prescriptions = prescriptionRepo.getPrescriptionsByUserUid(user.get());

            for (Prescription p : prescriptions) {
                responses.add(new GetPrescriptionsResponse(p.getId(), p.getName(), p.getDate()));
            }

            return responses;
        }

        return responses;
    }

    @Override
    public GetPrescriptionResponse getPrescription(int prescriptionId, String id) {
        Optional<Prescription> prescription = prescriptionRepo.getPrescriptionById(prescriptionId);
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
}
