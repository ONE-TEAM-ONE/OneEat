package com.sparta.oneeat.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "P_PAYMENT")
@Getter
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="PAYMENT_ID", nullable = false)
    private String id;

    @OneToOne
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Order order;

    @Column(name = "PAYMENT_STATUS", nullable = false)
    private PaymentStatusEnum status;

}
