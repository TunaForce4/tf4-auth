package com.tunaforce.auth.entity;

import lombok.Getter;

@Getter
public enum UserRole {
    MASTER("MASTER"),
    DELIVERY("DELIVERY"),
    COMPANY("COMPANY"),
    HUB("HUB");

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    // String으로부터 UserRole을 찾는 메서드
    public static UserRole fromString(String role) {
        for (UserRole userRole: UserRole.values()) {
            if (userRole.roleName.equalsIgnoreCase(role)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("Invalid role name: " + role);
    }
}
