package tech.bread.solt.doctornyangserver.controller;

import org.springframework.web.bind.annotation.*;
import tech.bread.solt.doctornyangserver.model.dto.request.DoneDosageRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.DosageRegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.SetPrivateDosageRequest;
import tech.bread.solt.doctornyangserver.model.entity.Dosage;
import tech.bread.solt.doctornyangserver.service.DosageService;

import java.util.List;

@RestController
@RequestMapping("/dosage")
public class DosageController {
    final DosageService dosageService;

    public DosageController(DosageService dosageService) {
        this.dosageService = dosageService;
    }

    @PostMapping("/register")
    public int registerDosage(@RequestBody DosageRegisterRequest request){
        return dosageService.registerDosage(request);
    }

    @PostMapping("/management")
    public Boolean tookMedicine(@RequestBody DoneDosageRequest request){
        return dosageService.toggleDosage(request);
    }

    @PostMapping("/private-set")
    public int setPrivateDosage(@RequestBody SetPrivateDosageRequest request){
        return dosageService.registerPrivateDosage(request);
    }

    @GetMapping("/show/{uid}")
    public List<Dosage> getMedicineDosage(@PathVariable("uid") int uid){
        return dosageService.getMedicineDosage(uid);
    }
}
