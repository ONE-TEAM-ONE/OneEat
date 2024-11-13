package com.sparta.oneeat.review.repository;

import com.sparta.oneeat.order.entity.Order;
import com.sparta.oneeat.review.entity.Review;
import com.sparta.oneeat.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    Optional<Review> findByUserAndOrder(User user, Order order);
}
