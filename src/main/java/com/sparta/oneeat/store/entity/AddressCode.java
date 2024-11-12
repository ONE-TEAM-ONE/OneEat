package com.sparta.oneeat.store.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name="P_ADDRESS_CODE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressCode {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="ADDRESS_CODE_ID", nullable = false)
    private UUID id;

    @Column(name = "ADDRESS_CODE_AREA", nullable = false)
    private String area;
}
