package com.sparta.oneeat.user.repository;

import com.sparta.oneeat.user.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, UUID> {
    Optional<UserAddress> findByUserIdAndAddress(Long id, String address);

    List<UserAddress> findByUserId(Long id);

    Optional<UserAddress> findByIdAndUserId(UUID id, Long userId);
}
