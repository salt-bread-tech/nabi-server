package tech.bread.solt.doctornyangserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.bread.solt.doctornyangserver.model.dto.request.AddIngestionRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.UpdateIngestionRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.GetCalorieInformResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.GetDietResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.GetIngestionTotalResponse;
import tech.bread.solt.doctornyangserver.service.DietService;

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
    public List<String> searchFoodList(@PathVariable("name") String name) {
        return dietService.getFoodList(name);
    }

    @GetMapping("/food/{name}/{num}")
    public GetCalorieInformResponse getCalorieInform(@PathVariable("name") String name, @PathVariable("num") int num) {
        return dietService.getCalorieInform(name, num);
    }

    @GetMapping("/diet/{uid}/today")
    public List<GetDietResponse> getDietToday(@PathVariable("uid") int uid) {
        return dietService.getDiet(uid, LocalDate.now());
    }

    @GetMapping("/diet/{uid}/{date}")
    public List<GetDietResponse> getDiet(@PathVariable("uid") int uid, @PathVariable("date") LocalDate date) {
        return dietService.getDiet(uid, date);
    }


    @GetMapping("/ingestion/total/{uid}/today")
    public GetIngestionTotalResponse getIngestionTotalToday(@PathVariable("uid") int uid) {
        return dietService.getIngestionTotal(uid, LocalDate.now());
    }

    @GetMapping("/ingestion/total/{uid}/{date}")
    public GetIngestionTotalResponse getIngestionTotal(@PathVariable("uid") int uid, @PathVariable("date") LocalDate date) {
        return dietService.getIngestionTotal(uid, date);
    }

    @PostMapping("/ingestion")
    public int addIngestion(@RequestBody AddIngestionRequest request) {
        return dietService.addIngestion(request);
    }

    @PostMapping("/ingestion/update")
    public int updateIngestion(@RequestBody UpdateIngestionRequest request) {
        return dietService.updateIngestion(request);
    }

    @GetMapping("/ingestion/{ingestionId}/delete")
    public int deleteIngestion(@PathVariable("ingestionId") int ingestionId) {
        return dietService.deleteIngestion(ingestionId);
    }
}
