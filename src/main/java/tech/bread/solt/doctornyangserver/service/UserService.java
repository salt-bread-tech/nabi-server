package tech.bread.solt.doctornyangserver.service;

import org.springframework.http.ResponseEntity;
import tech.bread.solt.doctornyangserver.model.dto.request.EnterBodyInformationRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.LoginRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.CountingDaysResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.LoginResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.RegisterResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.UserInfoResponse;


public interface UserService {
    ResponseEntity<? super RegisterResponse> register(RegisterRequest request);
    ResponseEntity<? super LoginResponse> login(LoginRequest request);
    int enterBodyInformation(EnterBodyInformationRequest request);
    UserInfoResponse showUser(int uid);
    CountingDaysResponse countingDays(String userId);
}