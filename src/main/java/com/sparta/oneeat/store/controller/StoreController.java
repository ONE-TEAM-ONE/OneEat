package com.sparta.oneeat.store.controller;

import com.sparta.oneeat.auth.service.UserDetailsImpl;
import com.sparta.oneeat.common.response.BaseResponseBody;
import com.sparta.oneeat.store.dto.CreateStoreReqDto;
import com.sparta.oneeat.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Store", description = "Store API")
public class StoreController {
    private final StoreService storeService;

    @Operation(summary = "가게 목록 조회", description = "자신의 배달지의 배달가능한 가게 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 목록 조회 성공"),
            @ApiResponse(responseCode = "500", description = "가게 목록 조회 실패")
    })
    @GetMapping("/store")
    public ResponseEntity<? extends BaseResponseBody> getStoreList(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String sort,
            @RequestParam("isAsc") boolean isAsc
    ){
        Long userId = userDetails.getUser().getId();

        log.info("userId : {}", userId);
        log.info("page : {}", page);
        log.info("size : {}", size);
        log.info("sort : {}", sort);
        log.info("isAsc : {}", isAsc);

        return ResponseEntity.status(200).body(BaseResponseBody.of(0, storeService.getStoreList(userId,(page-1), size, sort, isAsc)));
    }

    @Operation(summary = "가게 생성", description = "가게 정보를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "가게 생성 성공"),
            @ApiResponse(responseCode = "500", description = "가게 생성 실패")
    })
    @PostMapping("/store")
    public ResponseEntity<? extends BaseResponseBody> createStore(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Validated @RequestBody CreateStoreReqDto createStoreReqDto
    ){

        log.info("userId : {}", userDetails.getId());
        log.info("storeName : {}", createStoreReqDto.getName());
        log.info("category : {}", createStoreReqDto.getCategory());
        log.info("address : {}", createStoreReqDto.getAddress());
        log.info("DeliveryRegions : {}", createStoreReqDto.getDeliveryRegions());

        return ResponseEntity.status(201).body(BaseResponseBody.of(0, storeService.createStore(userDetails.getUser(), createStoreReqDto)));
    }

    @Operation(summary = "가게 상세 조회", description = "가게를 상세 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 상세 조회 성공"),
            @ApiResponse(responseCode = "500", description = "가게 상세 조회 실패")
    })
    @GetMapping("/store/{store_id}")
    public ResponseEntity<? extends BaseResponseBody> getStoreDetail(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable(name = "store_id") UUID storeId,
            @RequestParam(required = false) Integer menuPage,
            @RequestParam(required = false) Integer menuSize,
            @RequestParam(required = false) String menuSort,
            @RequestParam(required = false) Boolean menuIsAsc,
            @RequestParam(required = false) Integer reviewPage,
            @RequestParam(required = false) Integer reviewSize,
            @RequestParam(required = false) String reviewSort,
            @RequestParam(required = false) Boolean reviewIsAsc
    ) {
        log.info("userId : {}", userDetails.getId());
        log.info("storeId : {}", storeId);

        return ResponseEntity.ok(BaseResponseBody.of(0, storeService.getStoreDetail(storeId, menuPage, menuSize, menuSort, menuIsAsc,
                reviewPage, reviewSize, reviewSort, reviewIsAsc)));
    }
}
