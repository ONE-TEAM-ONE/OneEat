package com.sparta.oneeat.store.repository;

import com.sparta.oneeat.store.entity.Category;
import com.sparta.oneeat.store.entity.Store;
import com.sparta.oneeat.user.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository <Store, UUID> {

    List<Store> findAllByUser(User user);
    Optional<Store> findByIdAndUser(UUID storeId, User user);

    // 지정된 유저와 연관된 가게 조회
    Optional<Store> findByUser(User user);

    List<Store> findByCategory(Category category);
}
