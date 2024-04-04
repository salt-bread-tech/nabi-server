package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.EnterBodyInformationRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.LoginRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.UserInfoResponse;

public interface UserService {
    int register(RegisterRequest request);
    int login(LoginRequest request);
    int enterBodyInformation(EnterBodyInformationRequest request);

    UserInfoResponse showUser(int uid);
}
