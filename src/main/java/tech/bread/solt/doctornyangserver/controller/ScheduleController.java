package tech.bread.solt.doctornyangserver.controller;

import org.springframework.web.bind.annotation.*;
import tech.bread.solt.doctornyangserver.model.dto.request.ScheduleRegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.ScheduleListResponse;
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

    @GetMapping("/{date}")
    public Map<LocalDate, List<ScheduleListResponse>>
    getScheduleList(@PathVariable("date") LocalDate date, Principal principal) {
        return scheduleService.getScheduleList(date, principal.getName());
    }

    @GetMapping("/today")
    public Map<LocalDate, List<ScheduleListResponse>> getScheduleList(Principal principal) {
        return scheduleService.getScheduleList(LocalDate.now(), principal.getName());
    }
}
