package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.EnterBodyInformationRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.LoginRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.LoginResponse;

public interface UserService {
    int register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    int enterBodyInformation(EnterBodyInformationRequest request);
}
