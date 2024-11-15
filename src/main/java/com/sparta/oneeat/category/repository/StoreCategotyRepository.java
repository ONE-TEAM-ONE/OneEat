package com.sparta.oneeat.category.repository;

import com.sparta.oneeat.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StoreCategotyRepository extends JpaRepository<Category, UUID> {
    Optional<Category> findByCategoryName(String category);
}
