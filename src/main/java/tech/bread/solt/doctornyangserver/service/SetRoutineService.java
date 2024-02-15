package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.PrivateRoutineRegisterRequest;

public interface SetRoutineService {
    void registerPrivateRoutine(PrivateRoutineRegisterRequest request);
}
