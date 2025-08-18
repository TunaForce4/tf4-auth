package com.tunaforce.auth.service;

import com.tunaforce.auth.dto.request.LoginRequestDto;
import com.tunaforce.auth.dto.request.SignUpRequestDto;
import com.tunaforce.auth.dto.response.IdCheckResponseDto;
import com.tunaforce.auth.dto.response.LoginResponseDto;
import com.tunaforce.auth.dto.response.UserInfoResponseDto;
import com.tunaforce.auth.entity.User;
import com.tunaforce.auth.exception.LoginIdNotFoundException;
import com.tunaforce.auth.exception.UserAlreadyExistsException;
import com.tunaforce.auth.exception.UserNotFoundException;
import com.tunaforce.auth.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${service.application.name}")
    private String issuer;

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;

    private final SecretKey secretKey;

    public AuthService(UserRepository userRepository, @Value("${service.jwt.secret-key}") String secretKey, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByUserLoginId(loginRequestDto.userLoginId()).orElseThrow(
                () -> new LoginIdNotFoundException(loginRequestDto.userLoginId())
        );

        return new LoginResponseDto(Jwts.builder()
                .claim("id", user.getUserId().toString())
                .claim("username", user.getUsername())
                .claim("role", user.getRole().toString())
                .issuer(issuer)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact()
        );
    }

    public void signUp(SignUpRequestDto signUpRequestDto) {
        // 중복 계정 체크 (userLoginId 기준)
        if (existsByLoginId(signUpRequestDto.userLoginId())) {
            throw new UserAlreadyExistsException();
        }

        // DTO -> Entity 매핑 및 비밀번호 해시
        User user = toUserEntity(signUpRequestDto);

        // 저장
        userRepository.save(user);
    }

    private boolean existsByLoginId(String userLoginId) {
        return userRepository.findByUserLoginId(userLoginId).isPresent();
    }

    private User toUserEntity(SignUpRequestDto dto) {
        return User.builder()
                .userLoginId(dto.userLoginId())
                .username(dto.name())
                .password(passwordEncoder.encode(dto.password()))
                .tel(dto.tel())
                .role(dto.role())
                .slackId(dto.slackId())
                .build();
    }

    public IdCheckResponseDto checkId(String loginId) {
        if (existsByLoginId(loginId)) {
            throw new UserAlreadyExistsException();
        }
        return new IdCheckResponseDto("사용 가능한 Id 입니다.");
    }

    public UserInfoResponseDto getUserInfo(String id) {
        return UserInfoResponseDto.from(
                userRepository.findById(UUID.fromString(id))
                        .orElseThrow(UserNotFoundException::new)
        );
    }
}
