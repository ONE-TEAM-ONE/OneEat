package com.sparta.oneeat.user.repository;

import com.sparta.oneeat.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
