package tech.bread.solt.doctornyangserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.bread.solt.doctornyangserver.model.dto.request.GetMedicineRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterMedicineRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.GetMedicineDescriptionResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.GetMedicineResponse;
import tech.bread.solt.doctornyangserver.model.entity.Medicine;
import tech.bread.solt.doctornyangserver.service.MedicineService;

import java.util.List;

@RestController
public class MedicineController {

    final MedicineService medicineService;

    @Autowired
    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @GetMapping("/medicines/{name}")
    public List<String> getMedicineDescription(@PathVariable("name") String name) {
        return medicineService.getMedicineList(name);
    }

    @GetMapping("/medicine/{name}/{num}")
    public GetMedicineDescriptionResponse getMedicineDescriptionResponse(@PathVariable("name") String name, @PathVariable("num") int num) {
        return medicineService.getMedicineDescription(name, num);
    }

    @PostMapping("/medicine/register")
    public int registerMedicine(@RequestBody RegisterMedicineRequest request){
        return medicineService.registerMedicine(request);
    }

    @PostMapping("/medicine/info")
    public List<GetMedicineResponse> GetMedicineList(@RequestBody GetMedicineRequest request){
        return medicineService.getMedicineList(request);
    }
}