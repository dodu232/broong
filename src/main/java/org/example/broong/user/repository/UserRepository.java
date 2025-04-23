package org.example.broong.user.repository;

import org.example.broong.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{

    boolean existsByEmail(String email);
}
