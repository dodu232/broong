package org.example.broong.domain.menu.repository;

import org.example.broong.domain.menu.entity.MenuOptions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuOptionRepository extends JpaRepository<MenuOptions, Long> {
}
