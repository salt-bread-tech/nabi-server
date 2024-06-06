package tech.bread.solt.doctornyangserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.bread.solt.doctornyangserver.model.dto.request.ModifyUserRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.LoginRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.CountingDaysResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.LoginResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.RegisterResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.UserInfoResponse;
import tech.bread.solt.doctornyangserver.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController{
    final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<? super RegisterResponse> register(@RequestBody RegisterRequest request){
        return userService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<? super LoginResponse> login(@RequestBody LoginRequest request){
        return userService.login(request);
    }

//    @PutMapping()
//    public int modifyUser(@RequestBody ModifyUserRequest request, Principal p){
//        request.setId(p.getName());
//        return userService.modifyUser(request);
//    }

    @GetMapping()
    public UserInfoResponse getUser(Principal p) {
        return userService.getUser(p.getName());
    }

    @GetMapping("/d-day")
    public CountingDaysResponse countingDays(Principal p) {
        return userService.countingDays(p.getName());
    }

    @DeleteMapping()
    public boolean deleteUser(Principal p) {
        return userService.deleteUser(p.getName());
    }
}
