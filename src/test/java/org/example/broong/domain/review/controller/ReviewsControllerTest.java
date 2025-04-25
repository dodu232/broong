package org.example.broong.domain.review.controller;

import org.example.broong.domain.common.AuthUser;
import org.example.broong.domain.reviews.controller.ReviewsController;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.time.LocalTime;

import static org.example.broong.domain.user.enums.UserType.USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ReviewsController.class)
public class ReviewsControllerTest {

    @Mock
    private ReviewsRepository reviewsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private ReviewsServiceImpl reviewsService;

    @InjectMocks
    private ReviewsController reviewsController;

    @Test
    public void createSuccess() {
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
                testUser
        );
        Orders testOrder = new Orders(
                testUser,
                testStore,
                10000,
                "대기"
        );
        AuthUser testAuthUser = new AuthUser(1L, "email.com", USER);
        CreateReviewRequestDto testCreateReviewRequestDto = new CreateReviewRequestDto(
                1,
                "내용"
        );
        Long testOrderId = 1L;
        // when
        reviewsController.create(testAuthUser , testOrderId, testCreateReviewRequestDto);
        // then
        verify(reviewsService, times(1));
    }
}
