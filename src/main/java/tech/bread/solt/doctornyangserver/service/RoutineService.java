package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.RegisterRoutineRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.ShowRoutineRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.IncrementRoutinePerformRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.ShowRoutineResponse;

import java.util.List;

public interface RoutineService {
    int register(RegisterRoutineRequest request);

    int increment(IncrementRoutinePerformRequest request);

    List<ShowRoutineResponse> show(ShowRoutineRequest request);

}
