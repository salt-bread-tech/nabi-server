package tech.bread.solt.doctornyangserver.model.dto.response;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetPrescriptionResponse {
    private Date prescriptionDate;
    private List<MedicineTaking> medicineTakings;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MedicineTaking {
        String medicineName;
        Integer dailyDosage;
        Integer totalDosage;
        Integer onceDosage;
        String medicineDosage;
    }
}
