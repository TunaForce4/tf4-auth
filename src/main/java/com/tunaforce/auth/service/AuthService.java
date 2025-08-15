package com.tunaforce.auth.service;

import com.tunaforce.auth.dto.request.SignUpRequestDto;
import com.tunaforce.auth.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public void signUp(SignUpRequestDto request) {
        // TODO: Implement user creation logic (validate, encode password, save entity)
    }
}
