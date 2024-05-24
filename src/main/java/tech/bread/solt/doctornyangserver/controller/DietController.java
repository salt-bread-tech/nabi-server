package tech.bread.solt.doctornyangserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.bread.solt.doctornyangserver.model.dto.request.AddIngestionRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.UpdateIngestionRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.GetCalorieInformResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.GetDietResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.GetIngestionTotalResponse;
import tech.bread.solt.doctornyangserver.service.DietService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
public class DietController {
    final DietService dietService;

    @Autowired
    public DietController(DietService dietService) {
        this.dietService = dietService;
    }

    @GetMapping("/foods/{name}")
    public List<GetCalorieInformResponse> searchFoodList(@PathVariable("name") String name) {
        return dietService.getFoodList(name);
    }

    @GetMapping("/food/{name}/{num}")
    public GetCalorieInformResponse getCalorieInform(@PathVariable("name") String name, @PathVariable("num") int num) {
        return dietService.getCalorieInform(name, num);
    }

    @GetMapping("/diet/today")
    public List<GetDietResponse> getDietToday(Principal p) {
        return dietService.getDiet(p.getName(), LocalDate.now());
    }

    @GetMapping("/diet/{date}")
    public List<GetDietResponse> getDiet(@PathVariable("date") LocalDate date, Principal p) {
        return dietService.getDiet(p.getName(), date);
    }

    @GetMapping("/ingestion/total/today")
    public GetIngestionTotalResponse getIngestionTotalToday(Principal p) {
        return dietService.getIngestionTotal(p.getName(), LocalDate.now());
    }

    @GetMapping("/ingestion/total/{date}")
    public GetIngestionTotalResponse getIngestionTotal(@PathVariable("date") LocalDate date, Principal p) {
        return dietService.getIngestionTotal(p.getName(), date);
    }

    @PostMapping("/ingestion")
    public int addIngestion(@RequestBody AddIngestionRequest request, Principal p) {
        return dietService.addIngestion(request, p.getName());
    }

    @PutMapping("/ingestion")
    public int updateIngestion(@RequestBody UpdateIngestionRequest request, Principal p) {
        return dietService.updateIngestion(request);
    }

    @GetMapping("/ingestion/{ingestionId}/delete")
    public int deleteIngestion(@PathVariable("ingestionId") int ingestionId) {
        return dietService.deleteIngestion(ingestionId);
    }
}
