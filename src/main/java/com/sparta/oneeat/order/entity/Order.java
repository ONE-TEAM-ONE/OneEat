package com.sparta.oneeat.order.entity;

import com.sparta.oneeat.common.entity.BaseEntity;
import com.sparta.oneeat.payment.entity.Payment;
import com.sparta.oneeat.store.entity.Store;
import com.sparta.oneeat.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "P_ORDER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="ORDER_ID", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERS_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_ID")
    private Store store;

    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
    private Payment payment;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderMenu> orderMenuList;

    @Column(name = "ORDER_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderTypeEnum type;

    @Column(name = "ORDER_STATE", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;

    @Column(name = "ORDER_TOTALPRICE", nullable = false)
    private Integer totalPrice;

    @Column(name = "ORDER_ADDRESS")
    private String address;

    public void cancle(){
        this.status = OrderStatusEnum.PAYMENT_CANCELLED;
    }

    public void modifyStatus(OrderStatusEnum status){
        this.status = status;
    }
}
