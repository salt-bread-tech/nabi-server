package tech.bread.solt.doctornyangserver.service;

import org.springframework.http.ResponseEntity;
import tech.bread.solt.doctornyangserver.model.dto.request.ModifyUserRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.LoginRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.CountingDaysResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.LoginResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.RegisterResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.UserInfoResponse;


public interface UserService {
    ResponseEntity<? super RegisterResponse> register(RegisterRequest request);
    ResponseEntity<? super LoginResponse> login(LoginRequest request);
    int modifyUser(ModifyUserRequest request);
    UserInfoResponse getUser(String id);
    CountingDaysResponse countingDays(String userId);
}