package com.sparta.oneeat.store.repository;

import com.sparta.oneeat.store.entity.Store;
import com.sparta.oneeat.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {
    // 지정된 유저와 연관된 가게 조회
    Optional<Store> findByUser(User user);
}
