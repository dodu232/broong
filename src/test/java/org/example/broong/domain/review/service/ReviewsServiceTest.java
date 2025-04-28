package org.example.broong.domain.review.service;

import org.example.broong.domain.menu.entity.Menu;
import org.example.broong.domain.order.entity.Order;
import org.example.broong.domain.order.repository.OrderRepository;
import org.example.broong.domain.order.service.OrderService;
import org.example.broong.domain.reviews.Entity.Reviews;
import org.example.broong.domain.reviews.dto.CreateReviewRequestDto;
import org.example.broong.domain.reviews.dto.UpdateReviewRequestDto;
import org.example.broong.domain.reviews.repository.ReviewsRepository;
import org.example.broong.domain.reviews.service.ReviewsServiceImpl;
import org.example.broong.domain.store.Category;
import org.example.broong.domain.store.entity.Store;
import org.example.broong.domain.store.repository.StoreRepository;
import org.example.broong.domain.store.service.StoreService;
import org.example.broong.domain.user.entity.User;
import org.example.broong.domain.user.repository.UserRepository;
import org.example.broong.domain.user.service.UserService;
import org.example.broong.global.exception.ApiException;
import org.example.broong.global.exception.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.example.broong.domain.menu.enums.MenuState.AVAILABLE;
import static org.example.broong.domain.order.enums.OrderStatus.BEFORE_ORDER;
import static org.example.broong.domain.user.enums.UserType.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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
    private StoreRepository storeRepository;

    @Mock
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

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
    private final Menu testMenu = new Menu(
            1L,
            testStore,
            "짜장면",
            6000,
            AVAILABLE,
            null
    );

    private final Order testOrder = new Order(
            testUser,
            testStore,
            testMenu,
            2,
            10000,
            BEFORE_ORDER,
            LocalDateTime.now()
    );
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

    // 리뷰 생성
    @Test
    @DisplayName("리뷰를 만들면 정상적으로 생성된다.")
    public void createSuccess() {
        // given
        ReflectionTestUtils.setField(testOrder, "id", 1L);
        given(orderService.getOrder(testOrder.getId())).willReturn(testOrder);
        ReflectionTestUtils.setField(testUser, "id", 1L);

        // when
        reviewsService.create(testUser.getId(), testOrder.getId(), testCreateReviewRequestDto);

        // then
        verify(reviewsRepository, times(1)).save(any());
    }

    // 리뷰 생성
    @Test
    @DisplayName("본인의 주문에만 리뷰를 생성할 수 있다.")
    public void createFail_1() {
        // given
        ReflectionTestUtils.setField(testOrder, "id", 1L);
        given(orderService.getOrder(testOrder.getId())).willReturn(testOrder);
        ReflectionTestUtils.setField(testUser, "id", 2L);

        Long testOrderId = 1L;
        Long testUserId = 1L;
        // when
        ApiException exception = assertThrows(ApiException.class, () -> reviewsService.create(testUserId, testOrderId, testCreateReviewRequestDto));
        // then
        assertEquals("본인의 주문에만 리뷰를 남길 수 있습니다.", exception.getMessage());
    }

    // 리뷰 생성
    @Test
    @DisplayName("주문이 없을 경우 API 예외를 던져준다.")
    public void createFail_2() {
        // given
        ReflectionTestUtils.setField(testOrder, "id", 1L);
        ReflectionTestUtils.setField(testUser, "id", 1L);
        Long testUserId = 1L;
        Long testOrderId = 2L;
        // when
        doThrow(new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "존재하지 않는 주문입니다."))
                .when(orderService).getOrder(testOrderId);

        ApiException exception = assertThrows(ApiException.class, () -> reviewsService.create(testUserId, testOrderId, testCreateReviewRequestDto));
        // then
        assertEquals("존재하지 않는 주문입니다.", exception.getMessage());
    }

    // 리뷰 생성
    @Test
    @DisplayName("주문에 이미 리뷰가 있을 경우 API 예외를 던져준다.")
    public void createFail_3() {
        // given
        ReflectionTestUtils.setField(testOrder, "id", 1L);
        given(orderService.getOrder(testOrder.getId())).willReturn(testOrder);
        ReflectionTestUtils.setField(testUser, "id", 1L);

        Long testUserId = 1L;
        Long testOrderId = 1L;

        given(reviewsRepository.existsByOrderId_Id(testOrderId)).willReturn(true);

        // when
        ApiException exception = assertThrows(ApiException.class, () -> reviewsService.create(testUserId, testOrderId, testCreateReviewRequestDto));
        // then
        assertEquals("리뷰를 작성하신 주문입니다.", exception.getMessage());
    }

    // 리뷰 생성
    @Test
    @DisplayName("리뷰를 작성할 주문이 폐업한 가게의 주문인 경우 API 예외를 던져준다.")
    public void createFail_4() {
        //given
        ReflectionTestUtils.setField(testOrder, "id", 1L);
        given(orderService.getOrder(testOrder.getId())).willReturn(testOrder);

        ReflectionTestUtils.setField(testUser, "id", 1L);
        Long testUserId = 1L;
        Long testOrderId = 1L;

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
        verify(reviewsRepository, times(1)).findReviewListByStoreId(any(), any());
    }

    // 리뷰 조회
    @Test
    @DisplayName("조회하는 가게가 없거나 폐업한 경우 API 예외를 던져준다.")
    public void getFail_StoreExistsFalse() {
        // given
        ReflectionTestUtils.setField(testStore, "id", 1L);
        Pageable testPageable = PageRequest.of(0, 10);
        Long testStoreId = 2L;

        // when
        doThrow(new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "존재하지 않는 가게입니다."))
                .when(storeService).checkActiveStore(testStoreId);

        // then
        ApiException exception = assertThrows(ApiException.class, () -> reviewsService.getReviewsListByStore(testStoreId, testPageable));
        assertEquals("존재하지 않는 가게입니다.", exception.getMessage());
    }

    // 리뷰 수정
    @Test
    @DisplayName("리뷰를 수정하면 정상적으로 수정된다.")
    public void updateSuccess() {
        //given
        ReflectionTestUtils.setField(testReview, "id", 1L);
        ReflectionTestUtils.setField(testUser, "id", 1L);
        given(reviewsRepository.findById(1L)).willReturn(Optional.of(testReview));

        // when
        reviewsService.updateById(testUser.getId(), testReview.getId(), testUpdateReviewRequestDto);

        // then
        Reviews updateReview = reviewsRepository.findById(testReview.getId()).orElseThrow();

        assertEquals(testReview, updateReview);
    }

    // 리뷰 수정
    @Test
    @DisplayName("수정할 리뷰가 없는 경우 API 예외를 던져준다.")
    public void updateFail_1() {
        // given
        ReflectionTestUtils.setField(testReview, "id", 1L);
        ReflectionTestUtils.setField(testUser, "id", 1L);
        given(reviewsRepository.findById(1L)).willReturn(Optional.empty());

        // when
        ApiException exception = assertThrows(ApiException.class, () -> reviewsService.updateById(testUser.getId(), testReview.getId(), testUpdateReviewRequestDto));
        // then
        assertEquals("존재하지 않는 리뷰입니다.", exception.getMessage());
    }

    // 리뷰 수정
    @Test
    @DisplayName("수정할 리뷰가 삭제된 경우 API 예외를 던져준다.")
    public void updateFail_2() {
        // given
        ReflectionTestUtils.setField(testReview, "id", 1L);
        ReflectionTestUtils.setField(testUser, "id", 1L);
        ReflectionTestUtils.setField(testReview, "deletedAt", LocalDateTime.now());
        given(reviewsRepository.findById(1L)).willReturn(Optional.of(testReview));

        // when
        ApiException exception = assertThrows(ApiException.class, () -> reviewsService.updateById(testUser.getId(), testReview.getId(), testUpdateReviewRequestDto));
        // then
        assertEquals("존재하지 않는 리뷰입니다.", exception.getMessage());
    }

    // 리뷰 수정
    @Test
    @DisplayName("본인의 리뷰가 아닌 경우 API 예외를 던져준다.")
    public void updateFail_3() {
        // given
        ReflectionTestUtils.setField(testReview, "id", 1L);
        ReflectionTestUtils.setField(testUser, "id", 1L);
        given(reviewsRepository.findById(1L)).willReturn(Optional.of(testReview));

        // when
        ApiException exception = assertThrows(ApiException.class, () -> reviewsService.updateById(2L, testReview.getId(), testUpdateReviewRequestDto));
        // then
        assertEquals("본인의 리뷰만 수정할 수 있습니다.", exception.getMessage());
    }

    // 리뷰 삭제
    @Test
    @DisplayName("리뷰를 삭제하면 정상적으로 삭제된다.")
    public void deleteSuccess() {
        //given
        ReflectionTestUtils.setField(testReview, "id", 1L);
        ReflectionTestUtils.setField(testUser, "id", 1L);
        given(reviewsRepository.findById(1L)).willReturn(Optional.of(testReview));

        // when
        reviewsService.deleteById(testUser.getId(), testReview.getId());

        //then
        Reviews deletedReview = reviewsRepository.findById(testReview.getId()).orElseThrow();
        assertEquals(testReview, deletedReview);
    }

    // 리뷰 삭제
    @Test
    @DisplayName("삭제할 리뷰를 찾지 못하는 경우 API 예외를 던져준다.")
    public void deleteFail_1() {
        //given
        ReflectionTestUtils.setField(testUser, "id", 1L);
        given(reviewsRepository.findById(1L)).willReturn(Optional.empty());

        // when
        ApiException exception = assertThrows(ApiException.class, () -> reviewsService.deleteById(testUser.getId(), 1L));
        //then
        assertEquals("존재하지 않는 리뷰입니다.", exception.getMessage());
    }

    // 리뷰 삭제
    @Test
    @DisplayName("삭제할 리뷰가 본인의 리뷰가 아닐 경우 API 예외를 던져준다.")
    public void deleteFail_2() {
        //given
        ReflectionTestUtils.setField(testReview, "id", 1L);
        ReflectionTestUtils.setField(testUser, "id", 2L);
        given(reviewsRepository.findById(1L)).willReturn(Optional.of(testReview));

        // when
        ApiException exception = assertThrows(ApiException.class, () -> reviewsService.deleteById(1L, testReview.getId()));
        //then
        assertEquals("본인의 리뷰만 삭제할 수 있습니다.", exception.getMessage());
    }

    // 리뷰 삭제
    @Test
    @DisplayName("삭제할 리뷰가 이미 삭제된 경우 API 예외를 던져준다.")
    public void deleteFail_3() {
        //given
        ReflectionTestUtils.setField(testReview, "id", 1L);
        ReflectionTestUtils.setField(testReview, "deletedAt", LocalDateTime.now());
        given(reviewsRepository.findById(1L)).willReturn(Optional.of(testReview));

        // when
        ApiException exception = assertThrows(ApiException.class, () -> reviewsService.deleteById(testUser.getId(), testReview.getId()));
        //then
        assertEquals("존재하지 않는 리뷰입니다.", exception.getMessage());
    }
}