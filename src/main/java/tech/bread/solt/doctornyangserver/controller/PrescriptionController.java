package tech.bread.solt.doctornyangserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.bread.solt.doctornyangserver.model.dto.request.PostPrescriptionRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.GetPrescriptionsResponse;
import tech.bread.solt.doctornyangserver.service.PrescriptionService;

import java.util.List;

@RestController
@RequestMapping("/prescription")
public class PrescriptionController {

    final PrescriptionService prescriptionService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping("/prescription")
    public int addPrescription(@RequestBody PostPrescriptionRequest request) {
        return prescriptionService.addPrescription(request);
    }

    @GetMapping("/prescriptions")
    public List<GetPrescriptionsResponse> getPrescriptionsResponses(@RequestParam int uid) {
        return prescriptionService.getPrescriptions(uid);
    }


}
