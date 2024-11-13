package com.sparta.oneeat.menu.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuRequestDto {

    @NotBlank(message = "메뉴 이름을 입력하세요")
    private String name;

    private String description;

    @NotNull(message = "null 값은 허용하지 않습니다")
    private Boolean ai;

    @Min(value = 100, message = "금액은 100원 이상이어야 합니다")
    private Integer price;

    private String image;

    private AiRequestDto aiRequestDto;
}
