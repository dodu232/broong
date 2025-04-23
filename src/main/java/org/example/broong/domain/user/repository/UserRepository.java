package org.example.broong.domain.user.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import org.example.broong.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
