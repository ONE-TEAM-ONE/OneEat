package com.sparta.oneeat.payment.entity;

import com.sparta.oneeat.order.entity.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "P_PAYMENT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="PAYMENT_ID", nullable = false)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Order order;

    @Column(name = "PAYMENT_DETAIL")
    private String detail;

    @Column(name = "PAYMENT_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatusEnum status;

    public Payment(Order order){
        this.order = order;
        this.status = PaymentStatusEnum.NOT_PAID;
    }
}
