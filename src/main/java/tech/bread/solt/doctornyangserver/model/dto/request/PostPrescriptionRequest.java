package tech.bread.solt.doctornyangserver.model.dto.request;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostPrescriptionRequest {
    private String name;
    private LocalDate date;
}
