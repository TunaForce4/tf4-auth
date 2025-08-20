package com.tunaforce.auth.config;

import com.tunaforce.auth.entity.UserRole;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserRoleConverter implements Converter<String, UserRole> {
    @Override
    public UserRole convert(String source) {
        return UserRole.fromString(source);
    }
}
