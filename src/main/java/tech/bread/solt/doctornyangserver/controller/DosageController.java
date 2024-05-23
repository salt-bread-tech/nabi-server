package tech.bread.solt.doctornyangserver.controller;

import org.springframework.web.bind.annotation.*;
import tech.bread.solt.doctornyangserver.model.dto.request.DoneDosageRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.DosageRegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterCustomDosageRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.UpdateDosageRequest;
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
    public int register(@RequestBody DosageRegisterRequest request, Principal principal){
        request.setUserId(principal.getName());
        return dosageService.register(request);
    }

    @PutMapping("/toggle")
    public Boolean take(@RequestBody DoneDosageRequest request, Principal principal){
        request.setUserId(principal.getName());
        return dosageService.take(request);
    }

    @PutMapping()
    public int update(@RequestBody UpdateDosageRequest request, Principal principal) {
        request.setUserId(principal.getName());
        return dosageService.update(request);
    }

    @GetMapping()
    public List<ShowDosageResponse> getDosages(Principal principal){
        return dosageService.getDosages(principal.getName());
    }

    @DeleteMapping("/{dosageId}")
    public boolean delete(@PathVariable("dosageId") int dosageId){
        return dosageService.delete(dosageId);
    }

    @PostMapping("/custom")
    public int registerCustom(@RequestBody RegisterCustomDosageRequest request, Principal principal) {
        request.setUserId(principal.getName());
        return dosageService.customize(request);
    }
}
