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

    @DeleteMapping()
    public ResponseEntity<Void> removeAccount(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = true) String token) {
        userService.removeAccount(token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "userId") String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
