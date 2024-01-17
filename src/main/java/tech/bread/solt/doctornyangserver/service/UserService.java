package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.LoginRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.PrescriptionRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterRequest;

public interface UserService {
    int register(RegisterRequest request);
    int login(LoginRequest request);

    int addPrescription(PrescriptionRequest request);
}
