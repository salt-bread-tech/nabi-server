package tech.bread.solt.doctornyangserver.controller;

import org.springframework.web.bind.annotation.*;
import tech.bread.solt.doctornyangserver.model.dto.request.ScheduleRegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.WeeklyCalendarRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.ScheduleListResponse;
import tech.bread.solt.doctornyangserver.model.entity.Schedule;
import tech.bread.solt.doctornyangserver.service.ScheduleService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService){
        this.scheduleService = scheduleService;
    }

    @PostMapping()
    public int registerSchedule(@RequestBody ScheduleRegisterRequest request, Principal principal){
        request.setId(principal.getName());
        return scheduleService.register(request);
    }

    @DeleteMapping("/{scheduleId}")
    public boolean deleteSchedule(@PathVariable("scheduleId") int scheduleId){
        return scheduleService.delete(scheduleId);
    }

    @PostMapping("/week")
    public Map<LocalDate, List<ScheduleListResponse>> getScheduleList(
            @RequestBody WeeklyCalendarRequest request,
            Principal principal) {
        request.setId(principal.getName());
        return scheduleService.getScheduleList(request);
    }
}
