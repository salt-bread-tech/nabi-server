package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.ScheduleRegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.ScheduleListResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ScheduleService {
    int register(ScheduleRegisterRequest request);
    boolean delete(int scheduleId);
    Map<LocalDate, List<ScheduleListResponse>> getScheduleList(LocalDate date, String id);
}
