package com.sparta.oneeat.review.Service;

import com.sparta.oneeat.common.exception.CustomException;
import com.sparta.oneeat.common.exception.ExceptionType;
import com.sparta.oneeat.order.entity.Order;
import com.sparta.oneeat.order.repository.OrderRepository;
import com.sparta.oneeat.review.dto.CreateReviewReqDto;
import com.sparta.oneeat.review.dto.CreateReviewResDto;
import com.sparta.oneeat.review.dto.ModifyReviewReqDto;
import com.sparta.oneeat.review.dto.ReviewListDto;
import com.sparta.oneeat.review.entity.Review;
import com.sparta.oneeat.review.repository.ReviewRepository;
import com.sparta.oneeat.store.entity.Store;
import com.sparta.oneeat.store.repository.StoreRepository;
import com.sparta.oneeat.user.entity.User;
import com.sparta.oneeat.user.entity.UserRoleEnum;
import com.sparta.oneeat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService{

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    @Override
    @Transactional
    public CreateReviewResDto createReview(long userId, UUID orderId, CreateReviewReqDto createReviewReqDto) {

        // 유저 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.INTERNAL_SERVER_ERROR));
        // 주문 조회
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(ExceptionType.ORDER_NOT_EXIST));

        // 해당 유저의 주문인지 확인
        if(!Objects.equals(user.getId(), order.getUser().getId())) throw new CustomException(ExceptionType.ACCESS_DENIED);

        // 리뷰가 존재하는지 확인
        if(reviewRepository.findByUserAndOrder(user, order).isPresent()) throw new CustomException(ExceptionType.REVIEW_ALREADY_EXIST);

        // 이미 리뷰가 있는가 주문이라면

        // 리뷰 엔티티 생성
        Review saved = reviewRepository.save(new Review(user, order, createReviewReqDto));

        return new CreateReviewResDto(saved.getId());
    }

    @Override
    public Page<ReviewListDto> getReviewList(UUID storeId, int page, int size, String sort, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort1 = Sort.by(direction, sort);
        Pageable pageable = PageRequest.of(page, size, sort1);

        Store store = storeRepository.findById(storeId).orElseThrow(() -> new CustomException(ExceptionType.INTERNAL_SERVER_ERROR));

        Page<Review> reviewList = reviewRepository.findAllByStoreAndDeletedAtIsNull(store, pageable);

        return reviewList.map(ReviewListDto::new);
    }

    @Override
    @Transactional
    public void modifyReview(long userId, UUID orderId, UUID reviewId, ModifyReviewReqDto modifyReviewReqDto) {

        // 유저 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.INTERNAL_SERVER_ERROR));
        // 리뷰 조회
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(ExceptionType.REVIEW_NOT_FOUND));

        if(!Objects.equals(user.getId(), review.getUser().getId())) throw new CustomException(ExceptionType.REVIEW_ACCESS_DENIED);

        review.modifyReview(modifyReviewReqDto);

    }

    @Override
    @Transactional
    public void softDeleteReview(long userId, UUID reviewId) {

        // 유저 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.INTERNAL_SERVER_ERROR));

        // 리뷰 조회
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(ExceptionType.REVIEW_NOT_FOUND));

        // 권한 조회
        if(!Objects.equals(user.getId(), review.getUser().getId())) throw new CustomException(ExceptionType.REVIEW_ACCESS_DENIED);

        // 논리적 삭제
        review.softDelete(userId);
    }

    @Override
    @Transactional
    public void hardDeleteReview(long userId, UUID reviewId) {

        // 유저 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.INTERNAL_SERVER_ERROR));

        // 관리자 권한 조회
        if(user.getRole() == UserRoleEnum.CUSTOMER || user.getRole() == UserRoleEnum.OWNER) throw new CustomException(ExceptionType.ONLY_ADMIN_ACCESS);

        // 리뷰 조회
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(ExceptionType.REVIEW_NOT_FOUND));

        // 숨김 처리 여부 조회
        if(review.getDeletedAt() == null) throw new CustomException(ExceptionType.SOFT_DELETE_ONLY);

        // 삭제
        reviewRepository.delete(review);

    }
}
