package org.example.broong.domain.review.service;

import org.example.broong.domain.reviews.Entity.Reviews;
import org.example.broong.domain.reviews.dto.CreateReviewRequestDto;
import org.example.broong.domain.reviews.dto.UpdateReviewRequestDto;
import org.example.broong.domain.reviews.repository.ReviewsRepository;
import org.example.broong.domain.reviews.service.ReviewsServiceImpl;
import org.example.broong.domain.store.Category;
import org.example.broong.domain.store.entity.Store;
import org.example.broong.domain.store.service.StoreService;
import org.example.broong.domain.testOrder.Orders;
import org.example.broong.domain.testOrder.OrdersRepository;
import org.example.broong.domain.user.entity.User;
import org.example.broong.domain.user.repository.UserRepository;
import org.example.broong.domain.user.service.UserService;
import org.example.broong.global.exception.ApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
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
    private UserService userService;

    @Mock
    private StoreService storeService;

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private UserRepository userRepository;

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
//    private final Reviews testReview = new Reviews(
//            testUser,
//            testOrder,
//            testStore,
//            1,
//            "내용"
//    )
    private final CreateReviewRequestDto testCreateReviewRequestDto = new CreateReviewRequestDto(
            1,
            "내용"
    );
    private final Reviews testReview = new Reviews(
            testUser,
            testOrder,
            testStore,
            testCreateReviewRequestDto.getRating(),
            testCreateReviewRequestDto.getContents()
    );
    private final UpdateReviewRequestDto testUpdateReviewRequestDto = new UpdateReviewRequestDto(
            2,
            "수정된 내용"
    );

    @Test
    @DisplayName("리뷰를 만들면 정상적으로 된다.")
    public void createSuccess() {
        // given
        given(ordersRepository.findById(1L)).willReturn(Optional.of(testOrder));
        Long testOrderId = 1L;
        Long testUserId = 1L;
        // when
        reviewsService.create(testUserId, testOrderId, testCreateReviewRequestDto);
        // then
        verify(reviewsRepository, times(1)).save(any());
    }

    // 리뷰 생성: 주문이 존재하지 않을 때
    @Test
    @DisplayName("주문이 없을 경우 API 예외를 던져준다.")
    public void createFail_OrderIsNull() {
        // given
        Long testUserId = 1L;
        Long testOrderId = 1L;
        // when
        ApiException exception = assertThrows(ApiException.class, () -> reviewsService.create(testUserId, testOrderId, testCreateReviewRequestDto));
        // then
        assertEquals("존재하지 않는 주문입니다.", exception.getMessage());
    }

    // 리뷰 생성
    @Test
    @DisplayName("주문에 이미 리뷰가 있을 경우 API 예외를 던져준다.")
    public void createFail_OrderAlreadyHasReview() {
        // given
        Long testUserId = 1L;
        Long testOrderId = 1L;
        given(ordersRepository.findById(1L)).willReturn(Optional.of(testOrder));
        given(reviewsRepository.existsByOrderId_Id(testOrderId)).willReturn(true);
        // when
        ApiException exception = assertThrows(ApiException.class, () -> reviewsService.create(testUserId, testOrderId, testCreateReviewRequestDto));
        // then
        assertEquals("리뷰를 작성하신 주문입니다.", exception.getMessage());
    }

    // 리뷰 생성
    @Test
    @DisplayName("리뷰를 작성할 주문이 폐업한 가게의 주문인 경우 API 예외를 던져준다.")
    public void createFail_StoreIsClosed() {
        //given
        Long testUserId = 1L;
        Long testOrderId = 1L;

        given(ordersRepository.findById(1L)).willReturn(Optional.of(testOrder));

        ReflectionTestUtils.setField(testStore, "deletedAt", LocalDateTime.now());

        // when
        ApiException exception = assertThrows(ApiException.class, () -> reviewsService.create(testUserId, testOrderId, testCreateReviewRequestDto));
        // then
        assertEquals("존재하지 않는 가게입니다.", exception.getMessage());
    }

    // 리뷰 조회
    @Test
    @DisplayName("가게 ID로 리뷰를 조회하면 정상적으로 페이징 조회된다.")
    public void getSuccess() {
        // given
        Pageable testPageable = PageRequest.of(0, 10);
        Long testStoreId = 1L;

        // when
        reviewsService.getReviewsListByStore(testStoreId, testPageable);
        // then
        verify(reviewsRepository, times(1)).findReviewListByStoreId(any(),any());
    }

    // 리뷰 수정
    @Test
    @DisplayName("리뷰를 수정하면 정상적으로 수정된다.")
    public void updateSuccess() {
        //given
        ReflectionTestUtils.setField(testReview, "id", 1L);
        ReflectionTestUtils.setField(testUser, "id", 1L);
        given(reviewsRepository.findById(1L)).willReturn(Optional.of(testReview));
        Long testUserId = 1L;
        Long testReviewId = 1L;

        // when
        reviewsService.updateById(testUserId, testReviewId, testUpdateReviewRequestDto);
        // then
        Reviews updateReview = reviewsRepository.findById(testReviewId).orElseThrow();
        assertEquals(testReview, updateReview);
    }

    // 리뷰 삭제
    @Test
    @DisplayName("리뷰를 삭제하면 정상적으로 삭제된다.")
    public void deleteSuccess() {
        //given
        ReflectionTestUtils.setField(testReview, "id", 1L);
        ReflectionTestUtils.setField(testUser, "id", 1L);
        given(reviewsRepository.findById(1L)).willReturn(Optional.of(testReview));
        Long testUserId = 1L;
        Long testReviewId = 1L;

        // when
        reviewsService.deleteById(testUserId, testReviewId);

        //then
        Reviews deletedReview = reviewsRepository.findById(testReviewId).orElseThrow();
        assertEquals(testReview, deletedReview);
    }
}