package com.sparta.oneeat.menu.dto.request;

import java.util.UUID;
import lombok.Data;

@Data
public class AiRequestDto {

    private String request;

    private String response;

    private Long userId;

    private UUID menuId;

}

