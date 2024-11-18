package com.sparta.oneeat.store.controller;

import com.sparta.oneeat.auth.service.UserDetailsImpl;
import com.sparta.oneeat.common.response.BaseResponseBody;
import com.sparta.oneeat.store.dto.CreateStoreReqDto;
import com.sparta.oneeat.store.dto.UpdateStoreReqDto;
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

        return ResponseEntity.status(201).body(BaseResponseBody.of(0, storeService.createStore(userDetails.getId(), createStoreReqDto)));
    }

    @Operation(summary = "가게 상세 조회", description = "가게를 상세 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 상세 조회 성공"),
            @ApiResponse(responseCode = "500", description = "가게 상세 조회 실패")
    })
    @GetMapping("/store/{store_id}")
    public ResponseEntity<? extends BaseResponseBody> getStoreDetail(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable(name = "store_id") UUID storeId
    ){
        log.info("userId : {}", userDetails.getId());
        log.info("storeId : {}", storeId);

        return ResponseEntity.ok(BaseResponseBody.of(0, storeService.getStoreDetail(storeId)));
    }

    @Operation(summary = "가게 수정", description = "가게 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 수정 성공"),
            @ApiResponse(responseCode = "404", description = "가게를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "가게 수정 실패")
    })
    @PutMapping("/store/{store_id}")
    public ResponseEntity<? extends BaseResponseBody> updateStore(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable(name = "store_id") UUID storeId,
            @Validated @RequestBody UpdateStoreReqDto updateStoreReqDto
    ){
        log.info("userId : {}", userDetails.getId());
        log.info("storeId : {}", storeId);
        log.info("storeName : {}", updateStoreReqDto.getName());
        log.info("category : {}", updateStoreReqDto.getCategory());
        log.info("address : {}", updateStoreReqDto.getAddress());
        log.info("DeliveryRegions : {}", updateStoreReqDto.getDeliveryRegions());

        return ResponseEntity.ok(BaseResponseBody.of(0, storeService.updateStore(userDetails.getId(), storeId, updateStoreReqDto)));
    }

    @Operation(summary = "가게 숨김", description = "가게 숨김을 요청합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 숨김 성공"),
            @ApiResponse(responseCode = "500", description = "가게 숨김 실패")
    })
    @PatchMapping("/store/{store_id}")
    public ResponseEntity<? extends BaseResponseBody> hideStore(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable(name = "store_id") UUID storeId
    ){
        log.info("userId : {}", userDetails.getId());
        log.info("storeId : {}", storeId);

        storeService.hideStore(userDetails.getId(), storeId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(0, null));
    }

}
