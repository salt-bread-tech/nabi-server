package tech.bread.solt.doctornyangserver.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterRoutineRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.ShowRoutineRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.IncrementRoutinePerformRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.ShowRoutineResponse;
import tech.bread.solt.doctornyangserver.service.RoutineService;

import java.util.List;

@RestController
@RequestMapping("/routine")
public class RoutineController {
    final RoutineService routineService;

    public RoutineController(RoutineService routineService) {
        this.routineService = routineService;
    }

    @PostMapping("/register")
    public int registerRoutine(@RequestBody RegisterRoutineRequest request) {
        return routineService.register(request);
    }

    @PostMapping("/increment")
    public int incrementRoutinePerform(@RequestBody IncrementRoutinePerformRequest request){
        return routineService.increment(request);
    }

    @PostMapping("/list")
    public List<ShowRoutineResponse> showRoutine(@RequestBody ShowRoutineRequest request){
        return routineService.show(request);
    }

    }
}
