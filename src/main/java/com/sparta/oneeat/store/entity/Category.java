package com.sparta.oneeat.store.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name="P_STORE_CATEGORY")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="STORE_CATEGORY_ID", nullable = false)
    private UUID id;

    @Column(name = "STORE_CATEGORY_CATEGORY", nullable = false)
    private StoreCategoryEnum category;

}
