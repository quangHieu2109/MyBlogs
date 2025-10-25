package github.quanghieu2109.myblogs.controller;

import github.quanghieu2109.myblogs.dto.response.request.UserCreateRequest;
import github.quanghieu2109.myblogs.dto.response.response.ApiResponse;
import github.quanghieu2109.myblogs.entity.User;
import github.quanghieu2109.myblogs.repository.UsersRepository;
import github.quanghieu2109.myblogs.service.UserSevice;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class UsersController {
    UsersRepository usersRepo;
    UserSevice  userSevice;
    @GetMapping
    public String testApi(){

        return "test";
    }
    @PostMapping
    public ApiResponse register(@Valid @RequestBody UserCreateRequest request){
        return userSevice.register(request);
    }
}
