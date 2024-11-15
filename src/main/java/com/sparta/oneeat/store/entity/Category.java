package com.sparta.oneeat.store.entity;

import com.sparta.oneeat.common.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name="P_STORE_CATEGORY")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="STORE_CATEGORY_ID", nullable = false)
    private UUID id;

    @Column(name = "STORE_CATEGORY_CATEGORY", nullable = false)
    private String categoryName;

    public void deleteCategory(long userId) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = userId;
    }
}
