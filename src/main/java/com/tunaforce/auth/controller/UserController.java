package com.tunaforce.auth.controller;

import com.tunaforce.auth.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    // TODO: Search User

    // TODO: get User Info

    // TODO: get my info

    // TODO: edit user info

    // 게이트웨이 헤더 기반 자기 계정 삭제
    @DeleteMapping()
    public ResponseEntity<Void> removeAccount(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        userService.removeAccountByHeader(userIdHeader);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "userId") String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
