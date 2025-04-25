package org.example.broong.domain.review.service;

import org.example.broong.domain.reviews.Entity.Reviews;
import org.example.broong.domain.reviews.dto.CreateReviewRequestDto;
import org.example.broong.domain.reviews.repository.ReviewsRepository;
import org.example.broong.domain.reviews.service.ReviewsServiceImpl;
import org.example.broong.domain.store.Category;
import org.example.broong.domain.store.entity.Store;
import org.example.broong.domain.store.repository.StoreRepository;
import org.example.broong.domain.testOrder.Orders;
import org.example.broong.domain.testOrder.OrdersRepository;
import org.example.broong.domain.user.entity.User;
import org.example.broong.domain.user.repository.UserRepository;
import org.example.broong.global.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Optional;

import static org.example.broong.domain.user.enums.UserType.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
    private StoreRepository storeRepository;

    @Mock
    private OrdersRepository ordersRepository;

    // 사용할 테스트용 데이터
    private final User testUser = new User(
            "email.com",
            "qwerAsdf!",
            "누구인가",
            USER
    );
    private final Store testStore = new Store(
            "메가커피",
            Category.CAFE,
            LocalTime.parse("08:00"),
            LocalTime.parse("21:00"),
            10000,
            testUser
    );
    private final Orders testOrder = new Orders(
            testUser,
            testStore,
            10000,
            "대기"
    );
    private final CreateReviewRequestDto testCreateReviewRequestDto = new CreateReviewRequestDto(
            1,
            "내용"
    );
    private final Reviews testReview = new Reviews(
            testUser,
            testOrder,
            testStore,
            testCreateReviewRequestDto
    );

    // 리뷰 생성: 성공
    @Test
    public void createSuccess() {
        // given
        given(ordersRepository.findById(1L)).willReturn(Optional.of(testOrder));
        Long testOrderId = 1L;
        // when
        reviewsService.create(testUser, testOrderId, testCreateReviewRequestDto);
        // then
        verify(reviewsRepository, times(1)).save(any());
    }

    // 리뷰 생성: 주문이 존재하지 않을 때
    @Test
    public void createFail_OrderIsNull() {
        // given
        Long testOrderId = 1L;
        // when
        ApiException exception = assertThrows(ApiException.class, () -> reviewsService.create(testUser, testOrderId, testCreateReviewRequestDto));
        // then
        assertEquals("존재하지 않는 주문입니다.", exception.getMessage());
    }

    // 리뷰 생성: 주문에 리뷰가 이미 있는 경우
    @Test
    public void createFail_OrderAlreadyHasReview() {
        // given
        given(ordersRepository.findById(1L)).willReturn(Optional.of(testOrder));
        Long testOrderId = 1L;
        given(reviewsRepository.existsByOrderId_Id(testOrderId)).willReturn(true);
        // when
        ApiException exception = assertThrows(ApiException.class, () -> reviewsService.create(testUser, testOrderId, testCreateReviewRequestDto));
        // then
        assertEquals("리뷰를 작성하신 주문입니다.", exception.getMessage());
    }

    // 리뷰 생성: 주문이 폐업한 가게일 경우
    // 가게 폐업 로직 생성 후 테스트
}