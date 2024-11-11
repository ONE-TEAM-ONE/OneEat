package com.sparta.oneeat.review.entity;

import com.sparta.oneeat.order.entity.Order;
import com.sparta.oneeat.store.entity.Store;
import com.sparta.oneeat.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "P_REVIEW")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="REVIEW_ID", nullable = false)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "STORE_ID")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "USERS_ID")
    private User user;

    @Column(name = "REVIEW_CONTENT", nullable = false)
    private String content;

    @Column(name = "REVIEW_IMAGE", nullable = false)
    private String image;

    @Column(name = "REVIEW_RATING", nullable = false)
    private Integer rating;
}
