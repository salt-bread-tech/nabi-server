package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.ScheduleRegisterRequest;

public interface ScheduleService {
    int registerSchedule(ScheduleRegisterRequest request);
    int deleteSchedule(int scheduleId);
}
