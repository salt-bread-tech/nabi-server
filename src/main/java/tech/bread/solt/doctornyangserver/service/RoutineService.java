package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.RegisterRoutineRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.IncrementRoutinePerformRequest;
public interface RoutineService {
    int register(RegisterRoutineRequest request);

    int increment(IncrementRoutinePerformRequest request);

}
