package tech.bread.solt.doctornyangserver.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.bread.solt.doctornyangserver.model.dto.request.DeletePrivateRoutineRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.PrivateRoutineRegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.TogglePrivateRoutineRequest;
import tech.bread.solt.doctornyangserver.repository.SetRoutineRepo;
import tech.bread.solt.doctornyangserver.service.RoutineService;
import tech.bread.solt.doctornyangserver.service.ScheduleService;
import tech.bread.solt.doctornyangserver.service.SetRoutineService;

@RestController
@RequestMapping("/set-routine")
public class SetRoutineController {
    final SetRoutineService setRoutineService;

    public SetRoutineController(SetRoutineService setRoutineService) {
        this.setRoutineService = setRoutineService;
    }

    @PostMapping("/register")
    public void registerRoutine(@RequestBody PrivateRoutineRegisterRequest request){
        setRoutineService.registerPrivateRoutine(request);
    }

    @PostMapping("/delete")
    public int deleteRoutine(@RequestBody DeletePrivateRoutineRequest request){
        return setRoutineService.deletePrivateRoutine(request);
    }

    @PostMapping("/toggle")
    public int toggleRoutine(@RequestBody TogglePrivateRoutineRequest request){
        return setRoutineService.togglePrivateRoutine(request);
    }
}
