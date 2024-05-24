package tech.bread.solt.doctornyangserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterMedicineRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.UpdateMedicineRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.GetMedicineDescriptionResponse;
import tech.bread.solt.doctornyangserver.service.MedicineService;

import java.security.Principal;
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

    @PostMapping("/medicine")
    public int register(@RequestBody RegisterMedicineRequest request, Principal principal){
        return medicineService.register(request, principal.getName());
    }

    @DeleteMapping("/medicine/{medicineId}")
    public boolean delete(@PathVariable("medicineId") int medicineId) {
        return medicineService.delete(medicineId);
    }

    @PutMapping("/medicine")
    public int update(@RequestBody UpdateMedicineRequest request, Principal principal) {
        request.setUserId(principal.getName());
        return medicineService.update(request);
    }
}