package com.sparta.oneeat.store.repository;

import com.sparta.oneeat.store.entity.Store;
import com.sparta.oneeat.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository <Store, UUID> {

    List<Store> findAllByUser(User user);
    Optional<Store> findByIdAndUser(UUID storeId, User user);

    // 지정된 유저와 연관된 가게 조회
    Optional<Store> findByUser(User user);

    @Query("SELECT s FROM Store s WHERE s.id IN :storeIds")
    Page<Store> findAllByStoreIds(@Param("storeIds") List<UUID> storeIds, Pageable pageable);

}
