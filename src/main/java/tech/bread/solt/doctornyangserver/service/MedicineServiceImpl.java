package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.MedicineRegisterRequest;
import tech.bread.solt.doctornyangserver.model.entity.Dosage;
import tech.bread.solt.doctornyangserver.model.entity.Medicine;
import tech.bread.solt.doctornyangserver.repository.DosageRepo;
import tech.bread.solt.doctornyangserver.repository.MedicineRepo;
import tech.bread.solt.doctornyangserver.repository.PrescriptionRepo;

import java.time.LocalDate;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService{
    private final MedicineRepo medicineRepo;
    private final PrescriptionRepo prescriptionRepo;
    @Override
    public int registerMedicine(MedicineRegisterRequest request) {

        medicineRepo.save(Medicine.builder()
                .prescriptionId(prescriptionRepo.findOneById(request.getPrescriptionId()))
                .medicineName(request.getMedicineName())
                .dailyDosage(request.getDailyDosage())
                .onceDosage(request.getOnceDosage())
                .totalDosage(request.getTotalDosage())
                .build());

        return 200;
    }
}