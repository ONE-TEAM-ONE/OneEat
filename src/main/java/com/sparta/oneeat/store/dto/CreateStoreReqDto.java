package com.sparta.oneeat.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
public class CreateStoreReqDto {
    @NotBlank(message = "가게 이름을 입력해야합니다.")
    private String name;

    @NotBlank(message = "가게 카테고리를 입력해야합니다.")
    private String category;

    @NotBlank(message = "가게 주소를 입력해야합니다.")
    private String address;

    @NotBlank(message = "가게 설명을 입력해야합니다.")
    private String description;

    @NotNull(message = "가게 시작 시간을 입력해야합니다.")
    private LocalTime startTime;

    @NotNull(message = "가게 종료 시간을 입력해야합니다.")
    private LocalTime endTime;

    @NotBlank(message = "사업자 이름을 입력해야합니다.")
    private String owner;

    @NotNull(message = "최소 주문 금액을 입력해야합니다.")
    private Integer minPrice;

    @NotNull(message = "배달 가능 지역을 입력해야합니다.")
    private List<String> deliveryRegions;
}