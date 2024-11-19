package com.sparta.oneeat.menu.entity;

import com.sparta.oneeat.common.entity.BaseEntity;
import com.sparta.oneeat.menu.dto.request.AiRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "P_AI")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ai extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="AI_ID", nullable = false)
    private UUID id;

    @Column(name="AI_REQUEST", nullable = false)
    private String request;

    @Column(name="AI_RESPONSE", nullable = false)
    private String response;

    @Column(name="USERS_ID", nullable = false)
    private Long userId;

    @Column(name="MENU_ID", nullable = false)
    private UUID menuId;

    public Ai(AiRequestDto requestDto, long userId, UUID menuId) {
        this.request = requestDto.getRequest();
        this.response = requestDto.getResponse();
        this.userId = userId;
        this.menuId = menuId;
    }
}
