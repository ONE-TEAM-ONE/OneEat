package com.sparta.oneeat.store.entity;

import com.sparta.oneeat.category.entity.Category;
import com.sparta.oneeat.common.entity.BaseEntity;
import com.sparta.oneeat.menu.entity.Menu;
import com.sparta.oneeat.review.entity.Review;
import com.sparta.oneeat.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="P_STORE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="STORE_ID", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERS_ID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_CATEGORY_ID", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "store")
    private List<Review> reviewList;

    @OneToMany(mappedBy = "store")
    private List<Menu> menuList;

    @Column(name="STORE_NAME", nullable = false)
    private String name;

    @Column(name="STORE_ADDRESS", nullable = false)
    private String address;

    @Column(name="STORE_DESC", nullable = false)
    private String description;

    @Column(name="STORE_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private StoreStatusEnum status;

    @Column(name="STORE_START_TIME", nullable = false)
    private LocalTime startTime;

    @Column(name="STORE_END_TIME", nullable = false)
    private LocalTime endTime;

    @Column(name="STORE_OWNER")
    private String owner;

    @Column(name="STORE_MINPRICE")
    private Integer minPrice;

    public Store(User user, Category category, String name, String address, String description,
                 StoreStatusEnum status, LocalTime startTime, LocalTime endTime, String owner, Integer minPrice) {
        this.user = user;
        this.category = category;
        this.name = name;
        this.address = address;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.owner = owner;
        this.minPrice = minPrice;
    }

    public void updateStore(Category category, StoreStatusEnum status,
                            String name, String address, String description,
                       LocalTime startTime, LocalTime endTime, String owner, Integer minPrice) {
        this.category = category;
        this.status = status;
        this.name = name;
        this.address = address;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.owner = owner;
        this.minPrice = minPrice;
    }

    public void hideStore(long userId) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = userId;
    }

}
