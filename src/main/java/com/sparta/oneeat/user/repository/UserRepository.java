package com.sparta.oneeat.user.repository;

import com.sparta.oneeat.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);

    Optional<User> findByName(String username);

    Optional<User> findByNameAndDeletedAtIsNull(String username);

    Optional<User> findByNickname(String nickname);

    Optional<Object> findByEmail(String email);
}
