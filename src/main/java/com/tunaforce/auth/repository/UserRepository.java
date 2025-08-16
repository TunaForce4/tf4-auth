package com.tunaforce.auth.repository;

import com.tunaforce.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUserLoginId(String userLoginId);
}
