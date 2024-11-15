package com.sparta.oneeat.store.entity;

import com.sparta.oneeat.common.entity.BaseEntity;
import com.sparta.oneeat.menu.entity.Menu;
import com.sparta.oneeat.review.entity.Review;
import com.sparta.oneeat.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
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
    private Date startTime;

    @Column(name="STORE_END_TIME", nullable = false)
    private Date endTime;

    @Column(name="STORE_OWNER")
    private String owner;

    @Column(name="STORE_MINPRICE")
    private Integer minPrice;
}
