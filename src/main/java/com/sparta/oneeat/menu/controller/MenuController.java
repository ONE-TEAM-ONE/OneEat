package com.sparta.oneeat.menu.controller;

import com.sparta.oneeat.auth.service.UserDetailsImpl;
import com.sparta.oneeat.common.response.BaseResponseBody;
import com.sparta.oneeat.menu.dto.request.AiCallRequestDto;
import com.sparta.oneeat.menu.dto.request.MenuRequestDto;
import com.sparta.oneeat.menu.service.AiService;
import com.sparta.oneeat.menu.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Menu", description = "Menu API")
public class MenuController {

    private final AiService aiService;
    private final MenuService menuService;


    @Operation(summary = "ai 메뉴 설명", description = "ai에게 메뉴 설명을 요청 합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "ai 응답 성공"),
        @ApiResponse(responseCode = "500", description = "ai 응답 실패")
    })
    @PostMapping("/ai")
    public ResponseEntity<? extends BaseResponseBody> aiCall(
        //@AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody AiCallRequestDto requestDto
    ) {
        log.info("requestDto : {}", requestDto);

        return ResponseEntity.status(200)
            .body(BaseResponseBody.of(0, aiService.generateQuestion(2L, requestDto)));
    }

    @Operation(summary = "메뉴 생성", description = "메뉴 생성을 요청합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "메뉴 생성 성공"),
        @ApiResponse(responseCode = "500", description = "메뉴 생성 실패")
    })
    @PostMapping("/store/{storeId}/menu")
    public ResponseEntity<? extends BaseResponseBody> createMenu(
        //@AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody MenuRequestDto requestDto,
        @PathVariable UUID storeId) {

        return ResponseEntity.status(200)
            .body(BaseResponseBody.of(0, menuService.createMenu(requestDto, 1L, storeId)));
    }

    @Operation(summary = "메뉴 상세 조회", description = "메뉴 상세 조회를 요청합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "메뉴 조회 성공"),
        @ApiResponse(responseCode = "500", description = "메뉴 조회 실패")
    })
    @PostMapping("/store/{storeId}/menu/{menuId}")
    public ResponseEntity<? extends BaseResponseBody> getMenuDetail(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable UUID storeId,
        @PathVariable UUID menuId) {

        return ResponseEntity.status(200)
            .body(BaseResponseBody.of(0, menuService.getMenuDetail(userDetails, storeId, menuId)));
    }


}
