package tech.bread.solt.doctornyangserver.model.dto.request;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostPrescriptionRequest {
    private int uid;
    private Date date;
    private List<MedicineTaking> medicineTakings;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MedicineTaking {
        String medicineName;
        Integer dailyDosage;
        Integer totalDosage;
        Integer onceDosage;
        String medicineDosage;
    }
}