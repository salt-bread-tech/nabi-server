package tech.bread.solt.doctornyangserver.model.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetPrescriptionResponse {
    private String prescriptionName;
    private LocalDate prescriptionDate;
    private List<MedicineTaking> medicineTakings;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MedicineTaking {
        int medicineId;
        String medicineName;
        int once;
        int days;
        List<Integer> time;
        String dosage;
        Boolean registeredDosingSchedule;
    }
}
