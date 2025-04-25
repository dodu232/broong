package org.example.broong.domain.reviews.repository;

import org.example.broong.domain.reviews.Entity.Reviews;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

// 리뷰 레포지토리 클래스
public interface ReviewsRepository extends JpaRepository<Reviews, Long>, ReviewsCustomRepository {
    @EntityGraph(attributePaths = {"orderId"})
    boolean existsByOrderId_Id(long orderIdId);
}