package tech.bread.solt.doctornyangserver.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.bread.solt.doctornyangserver.model.dto.request.DoneDosageRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.DosageRegisterRequest;
import tech.bread.solt.doctornyangserver.service.DosageService;

@RestController
@RequestMapping("/dosage")
public class DosageController {
    final DosageService dosageService;

    public DosageController(DosageService dosageService) {
        this.dosageService = dosageService;
    }

    @PostMapping("/dosage-register")
    public int registerDosage(@RequestBody DosageRegisterRequest request){
        return dosageService.registerDosage(request);
    }

    @PostMapping("/dosage-management")
    public Boolean tookMedicine(@RequestBody DoneDosageRequest request){
        return dosageService.toggleDosage(request);
    }
}
