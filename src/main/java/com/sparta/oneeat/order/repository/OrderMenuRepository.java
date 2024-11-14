package com.sparta.oneeat.order.repository;

import com.sparta.oneeat.order.entity.OrderMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderMenuRepository extends JpaRepository<OrderMenu, UUID> {
}
