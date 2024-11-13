package com.sparta.oneeat.order.repository;

import com.sparta.oneeat.order.entity.Order;
import com.sparta.oneeat.store.entity.Store;
import com.sparta.oneeat.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    // 유저 주문 리스트 조회
    Page<Order> findAllByUser(User user, Pageable pageable);

    // 가게 주문 리스트 조회
    Page<Order> findAllByStore(Store store, Pageable pageable);
}
