package tech.bread.solt.doctornyangserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tech.bread.solt.doctornyangserver.service.MedicineService;

@RestController
public class MedicineController {

    final MedicineService medicineService;

    @Autowired
    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @GetMapping("/medicine/{name}")
    public String getMedicineDescription(@PathVariable("name") String name) {
        return medicineService.getMedicineDescription(name);
    }
}