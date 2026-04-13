package pro.hiking.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pro.hiking.auth.dto.LoginRequest;
import pro.hiking.auth.dto.RegisterRequest;
import pro.hiking.auth.dto.UserResponse;
import pro.hiking.auth.entity.User;
import pro.hiking.auth.service.AuthService;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request);
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail());
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}