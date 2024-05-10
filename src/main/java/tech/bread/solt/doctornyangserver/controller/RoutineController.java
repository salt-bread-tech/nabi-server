package tech.bread.solt.doctornyangserver.controller;

import org.springframework.web.bind.annotation.*;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterRoutineRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.UpdateRoutineRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.ShowRoutineResponse;
import tech.bread.solt.doctornyangserver.service.RoutineService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/routine")
public class RoutineController {
    final RoutineService routineService;

    public RoutineController(RoutineService routineService) {
        this.routineService = routineService;
    }

    @PostMapping()
    public int registerRoutine(@RequestBody RegisterRoutineRequest request, Principal principal) {
        request.setId(principal.getName());
        return routineService.register(request);
    }

    @PutMapping()
    public int updateRoutine(@RequestBody UpdateRoutineRequest request, Principal principal){
        request.setId(principal.getName());
        return routineService.update(request);
    }

    @GetMapping()
    public List<ShowRoutineResponse> showRoutine(Principal principal){
        return routineService.show(principal.getName());
    }

    @DeleteMapping("/{routineId}")
    public boolean deleteRoutine(@PathVariable("routineId") int routineId) {
        return routineService.delete(routineId);
    }
}
