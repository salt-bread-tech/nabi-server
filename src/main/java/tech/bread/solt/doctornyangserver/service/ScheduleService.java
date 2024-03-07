package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.ScheduleRegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.WeeklyCalendarRequest;
import tech.bread.solt.doctornyangserver.model.entity.Schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ScheduleService {
    int registerSchedule(ScheduleRegisterRequest request);
    int deleteSchedule(int scheduleId);
    Map<LocalDate, List<Schedule>> showWeeklySchedules(WeeklyCalendarRequest request);
}
