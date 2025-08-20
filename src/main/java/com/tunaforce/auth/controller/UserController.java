package com.tunaforce.auth.controller;

import com.tunaforce.auth.dto.request.UserInfoEditRequestDto;
import com.tunaforce.auth.dto.response.UserInfoListResponseDto;
import com.tunaforce.auth.dto.response.UserInfoResponseDto;
import com.tunaforce.auth.entity.UserRole;
import com.tunaforce.auth.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<UserInfoListResponseDto> searchUsers(
            @RequestParam(value = "name", required = false) String name,
            @RequestHeader(value = "X-Roles") UserRole headerRoles
    ){
        return ResponseEntity.ok(userService.searchUsers(name, headerRoles));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserInfoResponseDto> getUserInfo(
            @PathVariable UUID userId,
            @RequestHeader(value = "X-User-Id") UUID headerUserId,
            @RequestHeader(value = "X-Roles") UserRole headerRoles
    ) {
        return ResponseEntity.ok(userService.getUserInfo(userId, headerUserId, headerRoles));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Void> editUserInfo(
            @RequestBody UserInfoEditRequestDto dto,
            @PathVariable(value = "userId") UUID userId,
            @RequestHeader(value = "X-Roles") UserRole role
    ) {
        userService.editUserInfo(dto, userId, role);
        return ResponseEntity.ok().build();
    }

    // 자기 계정 삭제(게이트웨이가 X-User-Id 제공)
    @DeleteMapping()
    public ResponseEntity<Void> removeAccount(
            @RequestHeader(value = "X-User-Id") UUID requesterUserId,
            @RequestHeader(value = "X-Roles") UserRole requesterRole
    ) {
        userService.deleteUser(null, requesterUserId, requesterRole);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable(value = "userId") UUID targetUserId,
            @RequestHeader(value = "X-User-Id") UUID requesterUserId,
            @RequestHeader(value = "X-Roles")  UserRole requesterRole
    ) {
        userService.deleteUser(targetUserId, requesterUserId, requesterRole);
        return ResponseEntity.ok().build();
    }
}
