package com.sparta.oneeat.order.entity;

import com.sparta.oneeat.store.entity.Store;
import com.sparta.oneeat.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "P_ORDER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="ORDER_ID", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "USERS_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "STORE_ID")
    private Store store;

    @OneToOne(mappedBy = "order")
    private Payment payment;

    @OneToMany(mappedBy = "order")
    private List<OrderMenu> orderMenuList;

    @Column(name = "ORDER_TYPE", nullable = false)
    private OrderTypeEnum type;

    @Column(name = "ORDER_STATE", nullable = false)
    private OrderStateEnum state;

    @Column(name = "ORDER_TOTALPRICE", nullable = false)
    private Integer totalPrice;

    @Column(name = "ORDER_ADDRESS")
    private String address;

}
