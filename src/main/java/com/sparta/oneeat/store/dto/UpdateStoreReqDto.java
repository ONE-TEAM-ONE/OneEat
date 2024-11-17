package com.sparta.oneeat.store.dto;

import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
public class UpdateStoreReqDto {
    private String name;
    private String category;
    private String status;
    private String address;
    private String description;
    private LocalTime startTime;
    private LocalTime endTime;
    private String owner;
    private Integer minPrice;
    private List<String> deliveryRegions;
}