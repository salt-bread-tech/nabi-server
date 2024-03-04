package tech.bread.solt.doctornyangserver.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.bread.solt.doctornyangserver.model.dto.request.AddIngestionRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.GetCalorieInformResponse;
import tech.bread.solt.doctornyangserver.service.DietService;

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

    @PostMapping("/food")
    public int addIngestion(@RequestBody AddIngestionRequest request) {
        return dietService.addIngestion(request);
    }
}
