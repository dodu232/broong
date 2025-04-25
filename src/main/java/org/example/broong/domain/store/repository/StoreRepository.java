package org.example.broong.domain.store.repository;

import java.util.List;
import org.example.broong.domain.store.entity.Stores;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Stores, Long> {

    @EntityGraph(attributePaths = {"user"})
    List<Stores> findByUserId(@Param("userId") long userId);
}
