package tech.bread.solt.doctornyangserver.model.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.bread.solt.doctornyangserver.model.entity.MedicineInformation;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrescriptionRequest {
    private int uid;
    private List<MedicineTaking> medicineTakings;
    private Date date;

    static class MedicineTaking {
        String medicineName;
        Integer dailyDosage;
        Integer totalDosage;
        Integer onceDosage;
    }
}
