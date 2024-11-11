package com.sparta.oneeat.store.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="P_STORE_CATEGORY")
@Getter
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="STORE_CATEGORY_ID", nullable = false)
    private String id;

    @Column(name = "STORE_CATEGORY_CATEGORY", nullable = false)
    private StoreCategoryEnum category;

}
