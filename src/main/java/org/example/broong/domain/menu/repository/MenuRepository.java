package org.example.broong.domain.menu.repository;

import org.example.broong.domain.menu.entity.Menus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menus, Long> {
    List<Menus> findAllByStoreIdAndMenuStateNot(Long storeId, String menuState);
}
