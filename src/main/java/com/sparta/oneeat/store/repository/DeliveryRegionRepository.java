package com.sparta.oneeat.store.repository;

import com.sparta.oneeat.store.entity.DeliveryRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryRegionRepository extends JpaRepository<DeliveryRegion, Long> {
    Optional<DeliveryRegion> findByStoreId(UUID storeId);

    @Query(value = "SELECT * FROM p_delivery_region WHERE :userAddress = ANY(delivery_regions)", nativeQuery = true)
    List<DeliveryRegion> findAllByUserAddress(@Param("userAddress") String userAddress);
}