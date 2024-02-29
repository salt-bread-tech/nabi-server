package tech.bread.solt.doctornyangserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tech.bread.solt.doctornyangserver.model.dto.response.GetCalorieInformResponse;
import tech.bread.solt.doctornyangserver.service.DietService;

@RestController
public class DietController {
    final DietService dietService;

    @Autowired
    public DietController(DietService dietService) {
        this.dietService = dietService;
    }

    @GetMapping("/diet/{name}")
    public GetCalorieInformResponse getCalorieInform(@PathVariable("name") String name) {
        return dietService.getCalorieInform(name);
    }
}
