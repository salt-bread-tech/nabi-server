package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.PostPrescriptionRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.PostPrescriptionRequest.MedicineTaking;
import tech.bread.solt.doctornyangserver.model.dto.response.GetPrescriptionResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.GetPrescriptionsResponse;
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
    public int addPrescription(PostPrescriptionRequest request, String id) {
        Optional<User> user = userRepo.findById(id);
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
    public GetPrescriptionResponse getPrescription(int prescriptionId) {
        Optional<Prescription> prescription = prescriptionRepo.getPrescriptionById(prescriptionId);
        GetPrescriptionResponse response = new GetPrescriptionResponse();
        List<GetPrescriptionResponse.MedicineTaking> medicineTakings = new ArrayList<>();

        if (prescription.isPresent()) {
            Prescription p = prescription.get();
            List<Medicine> medicines = medicineRepo.findAllByPrescriptionId(p);

            for (Medicine m : medicines) {
                medicineTakings.add(GetPrescriptionResponse.MedicineTaking.builder()
                                .medicineName(m.getMedicineName())
                                .dailyDosage(m.getDailyDosage())
                                .totalDosage(m.getTotalDosage())
                                .onceDosage(m.getOnceDosage())
                                .medicineDosage(m.getMedicineDosage())
                                .build());
            }

            response = GetPrescriptionResponse.builder()
                    .prescriptionDate(p.getDate())
                    .medicineTakings(medicineTakings)
                    .build();
        }

        return response;
    }
}
