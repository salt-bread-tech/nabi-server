package tech.bread.solt.doctornyangserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.bread.solt.doctornyangserver.model.dto.request.PostPrescriptionRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.GetPrescriptionResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.GetPrescriptionsResponse;
import tech.bread.solt.doctornyangserver.service.PrescriptionService;

import java.security.Principal;
import java.util.List;

@RestController
public class PrescriptionController {

    final PrescriptionService prescriptionService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @GetMapping("/prescription/{pid}")
    public GetPrescriptionResponse getPrescriptionResponse(@PathVariable("pid") int pid) {
        return prescriptionService.getPrescription(pid);
    }

    @PostMapping("/prescriptions")
    public int addPrescription(@RequestBody PostPrescriptionRequest request, Principal p) {
        return prescriptionService.addPrescription(request, p.getName());
    }

    @GetMapping("/prescriptions")
    public List<GetPrescriptionsResponse> getPrescriptionsResponses(Principal p) {
        return prescriptionService.getPrescriptions(p.getName());
    }

}
