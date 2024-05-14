package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.RegisterRoutineRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.UpdateRoutineRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.ShowRoutineResponse;

import java.time.LocalDate;
import java.util.List;

public interface RoutineService {
    int register(RegisterRoutineRequest request);

    int update(UpdateRoutineRequest request);

    List<ShowRoutineResponse> show(LocalDate date, String id);

    boolean delete(int routineId);
}
