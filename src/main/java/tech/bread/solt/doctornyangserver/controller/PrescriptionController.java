package tech.bread.solt.doctornyangserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.bread.solt.doctornyangserver.model.dto.request.PostPrescriptionRequest;
import tech.bread.solt.doctornyangserver.service.PrescriptionService;

@RestController
@RequestMapping("/prescription")
public class PrescriptionController {

    final PrescriptionService prescriptionService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping("/prescriptions")
    public int addPrescriptions(@RequestBody PostPrescriptionRequest request) {
        return prescriptionService.addPrescriptions(request);
    }
}
