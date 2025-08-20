package com.tunaforce.auth.controller;

import com.tunaforce.auth.service.UserService;
import lombok.AllArgsConstructor;
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

    // 자기 계정 삭제(게이트웨이가 X-User-Id 제공)
    @DeleteMapping()
    public ResponseEntity<Void> removeAccount(
            @RequestHeader(value = "X-User-Id") String userIdHeader
    ) {
        userService.deleteByUserId(userIdHeader);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "userId") String userId) {
        userService.deleteByUserId(userId);
        return ResponseEntity.ok().build();
    }
}
