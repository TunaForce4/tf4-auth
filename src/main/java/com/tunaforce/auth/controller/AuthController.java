package com.tunaforce.auth.controller;

import com.tunaforce.auth.dto.request.IdCheckRequestDto;
import com.tunaforce.auth.dto.request.LoginRequestDto;
import com.tunaforce.auth.dto.request.SignUpRequestDto;
import com.tunaforce.auth.dto.response.IdCheckResponseDto;
import com.tunaforce.auth.dto.response.LoginResponseDto;
import com.tunaforce.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequestDto request) {
        authService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/id")
    public ResponseEntity<IdCheckResponseDto> idCheck(@RequestBody IdCheckRequestDto request) {
        return ResponseEntity.ok(authService.checkId(request.userLoginId()));
    }
}
