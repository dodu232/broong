package org.example.broong.domain.review.service;

import org.example.broong.domain.reviews.dto.CreateReviewRequestDto;
import org.example.broong.domain.reviews.repository.ReviewsRepository;
import org.example.broong.domain.reviews.service.ReviewsService;
import org.example.broong.domain.reviews.service.ReviewsServiceImpl;
import org.example.broong.domain.store.Category;
import org.example.broong.domain.store.entity.Store;
import org.example.broong.domain.testOrder.Orders;
import org.example.broong.domain.testOrder.OrdersRepository;
import org.example.broong.domain.user.entity.User;
import org.example.broong.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;

import static org.example.broong.domain.user.enums.UserType.USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ReviewsServiceTest {

    @Mock
    private ReviewsRepository reviewsRepository;

    @InjectMocks
    private ReviewsServiceImpl reviewsService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrdersRepository ordersRepository;

    // 리뷰 생성 성공
    @Test
    public void createReview() {
        // given
        User testUser = new User(
                "email.com",
                "qwerAsdf!",
                "누구인가",
                USER
        );
        Store testStore = new Store(
                "메가커피",
                Category.CAFE,
                LocalTime.parse("08:00"),
                LocalTime.parse("21:00"),
                10000,
                2L
        );
        Orders testOrder = new Orders(
                testUser,
                testStore,
                10000,
                "대기"
        );
        CreateReviewRequestDto testCreateReviewRequestDto = new CreateReviewRequestDto(
                1,
                "내용"
        );

        // when
        reviewsService.create(testUser, testOrder, testCreateReviewRequestDto);
        // then
        verify(reviewsRepository, times(1)).save(any());
    }
}