package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.RegisterRoutineRequest;
public interface RoutineService {
    int register(RegisterRoutineRequest request);
}
