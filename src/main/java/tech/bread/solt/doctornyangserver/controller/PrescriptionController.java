package tech.bread.solt.doctornyangserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.bread.solt.doctornyangserver.model.dto.request.PostPrescriptionRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.UpdatePrescriptionRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.GetPrescriptionResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.GetPrescriptionsByDateResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.GetPrescriptionsResponse;
import tech.bread.solt.doctornyangserver.service.PrescriptionService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
public class PrescriptionController {

    final PrescriptionService prescriptionService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @GetMapping("/prescriptions")
    public List<GetPrescriptionsResponse> getPrescriptions(Principal p) {
        return prescriptionService.getPrescriptions(p.getName());
    }

    @GetMapping("/prescriptions/{pid}")
    public GetPrescriptionResponse getPrescription(@PathVariable("pid") int pid, Principal p) {
        return prescriptionService.getPrescription(pid, p.getName());
    }

    @GetMapping("/prescriptions/date/{date}")
    public List<GetPrescriptionsByDateResponse> getPrescriptionsByDate(@PathVariable("date") LocalDate date, Principal p) {
        return prescriptionService.getPrescriptionsByDate(date, p.getName());
    }

    @PostMapping("/prescriptions")
    public int addPrescription(@RequestBody PostPrescriptionRequest request, Principal p) {
        return prescriptionService.addPrescription(request, p.getName());
    }

    @PutMapping("/prescription")
    public int update(@RequestBody UpdatePrescriptionRequest request) {
        return prescriptionService.update(request);
    }

    @DeleteMapping("/prescription/{prescriptionId}")
    public boolean delete(@PathVariable("prescriptionId") int prescriptionId) {
        return prescriptionService.delete(prescriptionId);
    }

}
