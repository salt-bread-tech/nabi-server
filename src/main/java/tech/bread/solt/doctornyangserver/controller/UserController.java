package tech.bread.solt.doctornyangserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.bread.solt.doctornyangserver.model.dto.request.EnterBodyInformationRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.LoginRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.LoginResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.RegisterResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.UserInfoResponse;
import tech.bread.solt.doctornyangserver.security.IdCheckRequestDto;
import tech.bread.solt.doctornyangserver.security.IdCheckResponseDto;
import tech.bread.solt.doctornyangserver.security.ResponseMessage;
import tech.bread.solt.doctornyangserver.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<? super RegisterResponse> register(@RequestBody RegisterRequest request){
        return userService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<? super LoginResponse> login(@RequestBody LoginRequest request){
        return userService.login(request);
    }

    @PostMapping("/enter-body-information")
    public int enterBodyInformation(@RequestBody EnterBodyInformationRequest request){
        return userService.enterBodyInformation(request);
    }

    @GetMapping("/show-info/{uid}")
    public UserInfoResponse showUserInformation(@PathVariable("uid") int uid) {
        return userService.showUser(uid);
    }

    @PostMapping("/id-check")
    public ResponseEntity<? super IdCheckResponseDto> idCheck(@RequestBody IdCheckRequestDto request) {
        return userService.idCheck(request);
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
