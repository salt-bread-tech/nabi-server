package tech.bread.solt.doctornyangserver.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ScheduleRegisterRequest {
    private int userUid;
    private String text;
    private LocalDateTime date;
}
