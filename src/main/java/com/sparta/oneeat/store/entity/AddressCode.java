package com.sparta.oneeat.store.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="P_ADDRESS_CODE")
@Getter
@NoArgsConstructor
public class AddressCode {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="ADDRESS_CODE_ID", nullable = false)
    private String id;

    @Column(name = "ADDRESS_CODE_AREA", nullable = false)
    private String area;
}
