package com.tunaforce.auth.repository;

import com.tunaforce.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUserLoginId(String userLoginId);

    // 전체(소프트삭제 제외)
    List<User> findByDeletedAtIsNull();

    // 이름 부분 일치(대소문자 무시) + 소프트삭제 제외
    List<User> findByUsernameContainingIgnoreCaseAndDeletedAtIsNull(String username);
}
