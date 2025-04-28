package org.example.broong.domain.user.repository;

import org.example.broong.domain.user.entity.User;
import org.example.broong.global.exception.ApiException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.example.broong.global.exception.ErrorType.NO_RESOURCE;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    // ID로 사용자 검색, 없으면 예외 발생
    default User findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, NO_RESOURCE, "존재하지 않는 유저 입니다."));
    }
}
