package tech.bread.solt.doctornyangserver.controller;

import org.springframework.web.bind.annotation.*;
import tech.bread.solt.doctornyangserver.model.dto.request.EnterBodyInformationRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.LoginRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.UserInfoResponse;
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
    public int login(@RequestBody LoginRequest request){
        return userService.login(request);
    }

    @PostMapping("/enter-body-information")
    public int enterBodyInformation(@RequestBody EnterBodyInformationRequest request){
        return userService.enterBodyInformation(request);
    }

    @GetMapping("/show-info/{uid}")
    public UserInfoResponse showUserInformation(@PathVariable("uid") int uid){
        return userService.showUser(uid);
    }
}
