package tech.bread.solt.doctornyangserver.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivateRoutineRegisterRequest {
    private int userUid;
    private int routineId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int maxPerform; // 하루 최대 반복 횟수
}
