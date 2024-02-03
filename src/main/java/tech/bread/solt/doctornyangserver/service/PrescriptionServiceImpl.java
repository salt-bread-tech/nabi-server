package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.PostPrescriptionRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.PostPrescriptionRequest.MedicineTaking;
import tech.bread.solt.doctornyangserver.model.entity.Medicine;
import tech.bread.solt.doctornyangserver.model.entity.Prescription;
import tech.bread.solt.doctornyangserver.model.entity.User;
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

    @Override
    public int addPrescriptions(PostPrescriptionRequest request) {
        Optional<User> user = userRepo.findById(request.getUid());
        List<MedicineTaking> medicineTakingList = request.getMedicineTakings();
        Prescription prescription;
        List<Medicine> medicineList = new ArrayList<>();

        if (user.isPresent()) {
            prescription = Prescription.builder()
                    .userUid(user.get())
                    .name(request.getDate().toString() + " 처방전")
                    .date(request.getDate())
                    .build();
            prescriptionRepo.save(prescription);

            for (MedicineTaking medicineTaking : medicineTakingList) {
                medicineList.add(Medicine.builder()
                                .medicineName(medicineTaking.getMedicineName())
                                .prescriptionId(prescription)
                                .dailyDosage(medicineTaking.getDailyDosage())
                                .totalDosage(medicineTaking.getTotalDosage())
                                .onceDosage(medicineTaking.getOnceDosage())
                                .medicineDosage(medicineTaking.getMedicineDosage())
                                .build());
            }
            medicineRepo.saveAll(medicineList);

            return 200;
        }

        return 0;
    }
}
