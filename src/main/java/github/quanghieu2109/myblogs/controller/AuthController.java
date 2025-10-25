package github.quanghieu2109.myblogs.controller;

import github.quanghieu2109.myblogs.dto.request.LoginRequest;
import github.quanghieu2109.myblogs.dto.response.ApiResponse;
import github.quanghieu2109.myblogs.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    AuthenticationService authService;
    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }
    @PostMapping("/logout")
    public ApiResponse logout(){
        return authService.logout();
    }

}
