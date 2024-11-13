package com.sparta.oneeat.menu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AiResponseDto { // ai 응답

    private String question;

    private String response;
}
