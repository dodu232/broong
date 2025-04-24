package org.example.broong.domain.review.controller;

import org.example.broong.domain.auto.dto.request.SignupRequestDto;
import org.example.broong.domain.reviews.dto.CreateReviewRequestDto;
import org.example.broong.domain.reviews.repository.ReviewsRepository;
import org.example.broong.domain.reviews.service.ReviewsService;
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

import static org.example.broong.domain.user.enums.UserType.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ReviewsControllerTest {

    @Mock
    private ReviewsRepository reviewsRepository;

    @InjectMocks
    private ReviewsService reviewsService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private OrdersRepository ordersRepository;

    @Test
    public void CreateReview() {

    }
}
