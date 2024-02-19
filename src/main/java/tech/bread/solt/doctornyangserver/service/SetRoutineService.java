package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.DeletePrivateRoutineRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.PrivateRoutineRegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.TogglePrivateRoutineRequest;

public interface SetRoutineService {
    void registerPrivateRoutine(PrivateRoutineRegisterRequest request);
    int deletePrivateRoutine(DeletePrivateRoutineRequest request);
    int togglePrivateRoutine(TogglePrivateRoutineRequest request);
}
