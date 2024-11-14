package com.sparta.oneeat.review.repository;

import com.sparta.oneeat.order.entity.Order;
import com.sparta.oneeat.review.entity.Review;
import com.sparta.oneeat.store.entity.Store;
import com.sparta.oneeat.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    Optional<Review> findByUserAndOrder(User user, Order order);

    // 가게 리뷰 목록 조회
    Page<Review> findAllByStoreAndDeletedAtIsNull(Store store, Pageable pageable);
}
