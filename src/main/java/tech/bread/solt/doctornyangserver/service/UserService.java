package tech.bread.solt.doctornyangserver.service;

import org.springframework.http.ResponseEntity;
import tech.bread.solt.doctornyangserver.model.dto.request.EnterBodyInformationRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.LoginRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.LoginResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.RegisterResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.UserInfoResponse;
import tech.bread.solt.doctornyangserver.security.IdCheckRequestDto;
import tech.bread.solt.doctornyangserver.security.IdCheckResponseDto;

public interface UserService {
    ResponseEntity<? super RegisterResponse> register(RegisterRequest request);
    ResponseEntity<? super LoginResponse> login(LoginRequest request);
    int enterBodyInformation(EnterBodyInformationRequest request);
    UserInfoResponse showUser(int uid);

    ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto);
}
