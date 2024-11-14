package com.sparta.oneeat.menu.entity;

import com.sparta.oneeat.common.entity.BaseEntity;
import com.sparta.oneeat.store.entity.Store;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "P_ITEM")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Menu extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="ITEM_ID", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_ID")
    private Store store;

    @Column(name="ITEM_NAME", nullable = false)
    private String name;

    @Column(name="ITEM_DESC")
    private String description;

    @Column(name="ITEM_AI", nullable = false)
    private Boolean ai;

    @Column(name="ITEM_PRICE", nullable = false)
    private Integer price;

    @Column(name="ITEM_IMAGE")
    private String image;

    @Column(name="ITEM_STATUS")
    @Enumerated(EnumType.STRING)
    private MenuStatusEnum status;


    public void delete(long userId) {
        this.deletedBy = userId;
        this.deletedAt = LocalDateTime.now();
    }
}
