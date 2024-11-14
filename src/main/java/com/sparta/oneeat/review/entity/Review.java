package com.sparta.oneeat.review.entity;

import com.sparta.oneeat.common.entity.BaseEntity;
import com.sparta.oneeat.order.entity.Order;
import com.sparta.oneeat.review.dto.CreateReviewReqDto;
import com.sparta.oneeat.review.dto.ModifyReviewReqDto;
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
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="REVIEW_ID", nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_ID")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERS_ID")
    private User user;

    @Column(name = "REVIEW_CONTENT", nullable = false)
    private String content;

    @Column(name = "REVIEW_IMAGE", nullable = false)
    private String image;

    @Column(name = "REVIEW_RATING", nullable = false)
    private Integer rating;

    // 리뷰 생성
    public Review(User user, Order order, CreateReviewReqDto createReviewReqDto){
        this.order = order;
        this.store = order.getStore();
        this.user = user;
        this.content = createReviewReqDto.getContent();
        this.rating = createReviewReqDto.getRating();
        this.image = createReviewReqDto.getImage();
    }

    // 리뷰 수정 메서드
    public void modifyReview(ModifyReviewReqDto modifyReviewReqDto) {
        this.content = modifyReviewReqDto.getContent();
        this.rating = modifyReviewReqDto.getRating();
        this.image = modifyReviewReqDto.getImage();
    }
}
