package org.example.broong.domain.menu.repository;

import org.example.broong.domain.menu.entity.Menu;
import org.example.broong.domain.menu.enums.MenuState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllByStoreIdAndMenuStateNot(Long storeId, String menuState);

    List<Menu> findByStoreIdAndMenuStateNot(Long storeId, MenuState state);

}
