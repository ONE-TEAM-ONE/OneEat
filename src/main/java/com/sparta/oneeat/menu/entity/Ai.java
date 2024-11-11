package com.sparta.oneeat.menu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "P_AI")
@Getter
@NoArgsConstructor
public class Ai {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="AI_ID", nullable = false)
    private String id;

    @Column(name="AI_REQUEST")
    private String request;

    @Column(name="AI_RESPONSE")
    private String response;

    @Column(name="USERS_ID")
    private String userId;

    @Column(name="MENU_ID")
    private String menuId;
}
