package tech.bread.solt.doctornyangserver.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.bread.solt.doctornyangserver.model.dto.request.EnterBodyInformationRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.LoginRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.LoginResponse;
import tech.bread.solt.doctornyangserver.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    final UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public int register(@RequestBody RegisterRequest request){
        return userService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request){
        return userService.login(request);
    }

    @PostMapping("enter-body-information")
    public int enterBodyInformation(@RequestBody EnterBodyInformationRequest request){
        return userService.enterBodyInformation(request);
    }
}
