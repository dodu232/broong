package org.example.broong.domain.user.repository;

import org.example.broong.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{

    boolean existsByEmail(String email);
}
