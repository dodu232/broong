package org.example.broong.domain.reviews.repository;

import org.example.broong.domain.reviews.Entity.Reviews;
import org.example.broong.domain.store.entity.Store;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 리뷰 레포지토리 클래스
public interface ReviewsRepository extends JpaRepository<Reviews, Long> {
    List<Reviews> findAllByStoreIdAndUserId_DeletedAtIsNull(Store storeId);

    @EntityGraph(attributePaths = {"orderId"})
    boolean existsByOrderId_Id(long orderIdId);
}