package org.example.broong.domain.store.repository;

import java.util.List;
import org.example.broong.domain.store.entity.Store;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    @EntityGraph(attributePaths = {"user"})
    List<Store> findByUserId(@Param("userId") long userId);

    boolean existsByIdAndUser_Id(Long storeId, Long userId);

}
