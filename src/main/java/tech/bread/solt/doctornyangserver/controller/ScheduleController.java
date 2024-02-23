package tech.bread.solt.doctornyangserver.controller;

import org.springframework.web.bind.annotation.*;
import tech.bread.solt.doctornyangserver.model.dto.request.ScheduleRegisterRequest;
import tech.bread.solt.doctornyangserver.service.ScheduleService;

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
}
