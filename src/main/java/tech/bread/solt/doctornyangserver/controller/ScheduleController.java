package tech.bread.solt.doctornyangserver.controller;

import org.springframework.web.bind.annotation.*;
import tech.bread.solt.doctornyangserver.model.dto.request.ScheduleRegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.ShowWeightManagementRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.WeeklyCalendarRequest;
import tech.bread.solt.doctornyangserver.model.entity.Schedule;
import tech.bread.solt.doctornyangserver.service.ScheduleService;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService){
        this.scheduleService = scheduleService;
    }

    @PostMapping("/register")
    public int registerSchedule(@RequestBody ScheduleRegisterRequest request){
        return scheduleService.registerSchedule(request);
    }

    @GetMapping("/delete")
    public int deleteSchedule(@RequestParam int scheduleId){
        return scheduleService.deleteSchedule(scheduleId);
    }

    @PostMapping("/weekly-calendar")
    public Map<LocalDate, List<Schedule>> showWeeklyCalendar(@RequestBody WeeklyCalendarRequest request){
        return scheduleService.showWeeklySchedules(request);
    }
}
