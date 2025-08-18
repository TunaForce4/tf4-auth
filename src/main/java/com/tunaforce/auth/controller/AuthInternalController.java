package com.tunaforce.auth.controller;

import com.tunaforce.auth.dto.response.UserInfoResponseDto;
import com.tunaforce.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/internal")
public class AuthInternalController {

    private final AuthService authService;

    @GetMapping("/users/{id}")
    public ResponseEntity<UserInfoResponseDto> getUserInfo(@PathVariable(value = "id") String id){
        return ResponseEntity.ok(authService.getUserInfo(id));
    }
}
