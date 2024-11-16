package com.sparta.oneeat.store.repository;

import com.sparta.oneeat.store.entity.DeliveryRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeliveryRegionRepository extends JpaRepository<DeliveryRegion, Long> {
    List<DeliveryRegion> findAllByStoreId(UUID storeId);

    List<DeliveryRegion> findAllByDeliveryRegion(String userAddress);
}