package tech.bread.solt.doctornyangserver.controller;

import org.springframework.web.bind.annotation.*;
import tech.bread.solt.doctornyangserver.model.dto.request.DeleteDosageRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.DoneDosageRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.DosageRegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.ShowDosageResponse;
import tech.bread.solt.doctornyangserver.service.DosageService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/dosage")
public class DosageController {
    final DosageService dosageService;

    public DosageController(DosageService dosageService) {
        this.dosageService = dosageService;
    }

    @PostMapping()
    public int registerDosage(@RequestBody DosageRegisterRequest request, Principal principal){
        request.setUserId(principal.getName());
        return dosageService.registerDosage(request);
    }

    @PostMapping("/management")
    public Boolean tookMedicine(@RequestBody DoneDosageRequest request){
        return dosageService.toggleDosage(request);
    }

    @GetMapping("/show/{uid}")
    public List<ShowDosageResponse> getMedicineDosage(@PathVariable("uid") int uid){
        return dosageService.getMedicineDosage(uid);
    }

    @PostMapping("/delete")
    public int deleteDosage(@RequestBody DeleteDosageRequest request){
        return dosageService.deleteDosage(request);
    }
}
