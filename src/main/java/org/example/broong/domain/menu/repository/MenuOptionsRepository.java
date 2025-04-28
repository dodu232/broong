package org.example.broong.domain.menu.repository;

import org.example.broong.domain.menu.entity.MenuOptions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuOptionsRepository extends JpaRepository<MenuOptions, Long> {

    List<MenuOptions> findByMenuId(Long menuId);
}
