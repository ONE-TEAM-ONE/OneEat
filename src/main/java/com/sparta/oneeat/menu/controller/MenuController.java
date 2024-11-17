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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody AiCallRequestDto requestDto
    ) {
        log.info("requestDto : {}", requestDto);

        return ResponseEntity.status(200)
            .body(BaseResponseBody.of(0, aiService.generateQuestion(userDetails.getId(), requestDto)));
    }

    @Operation(summary = "메뉴 생성", description = "메뉴 생성을 요청합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "메뉴 생성 성공"),
        @ApiResponse(responseCode = "500", description = "메뉴 생성 실패")
    })
    @PostMapping("/store/{storeId}/menu")
    public ResponseEntity<? extends BaseResponseBody> createMenu(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody MenuRequestDto requestDto,
        @PathVariable UUID storeId) {

        return ResponseEntity.status(201)
            .body(BaseResponseBody.of(0, menuService.createMenu(userDetails.getUser(), requestDto, storeId)));
    }

    @Operation(summary = "메뉴 목록 조회", description = "메뉴 목록을 요청합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "메뉴 목록 조회 성공"),
        @ApiResponse(responseCode = "500", description = "메뉴 목록 조회 실패")
    })
    @GetMapping("/store/{storeId}/menus")
    public ResponseEntity<? extends BaseResponseBody> getMenu(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable UUID storeId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "price") String sort) {

        return ResponseEntity.status(200)
            .body(BaseResponseBody.of(0, menuService.getMenuList(userDetails.getUser(), storeId, page, size, sort)));
    }

    @Operation(summary = "메뉴 수정", description = "메뉴를 수정합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "메뉴 수정 성공"),
        @ApiResponse(responseCode = "500", description = "메뉴 수정 실패")
    })
    @PutMapping("/store/{storeId}/menu/{menuId}")
    public ResponseEntity<? extends BaseResponseBody> updateMenu(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable UUID storeId,
        @PathVariable UUID menuId,
        @Valid @RequestBody MenuRequestDto requestDto) {

        return ResponseEntity.status(201)
            .body(BaseResponseBody.of(0,
                menuService.updateMenu(userDetails.getUser(), requestDto, storeId, menuId)));
    }

    @Operation(summary = "메뉴 상태 변경", description = "메뉴의 상태를 변경합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "메뉴 상태 변경 성공"),
        @ApiResponse(responseCode = "500", description = "메뉴 상태 변경 실패")
    })
    @PatchMapping("/store/{storeId}/menu/{menuId}/status")
    public ResponseEntity<? extends BaseResponseBody> updateMenuStatus(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable UUID storeId,
        @PathVariable UUID menuId) {

        menuService.updateMenuStatus(userDetails.getUser(), storeId, menuId);

        return ResponseEntity.status(200).body(BaseResponseBody.of(0, null));
    }

    @Operation(summary = "메뉴 숨김", description = "메뉴 숨김을 요청합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "메뉴 숨김 성공"),
        @ApiResponse(responseCode = "500", description = "메뉴 숨김 실패")
    })
    @PatchMapping("/store/{storeId}/menu/{menuId}")
    public ResponseEntity<? extends BaseResponseBody> hideMenu(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable UUID storeId,
        @PathVariable UUID menuId) {

        menuService.hideMenu(userDetails.getId(), storeId, menuId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(0, null));
    }

    @Operation(summary = "메뉴 삭제", description = "메뉴 삭제를 요청합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "메뉴 삭제 성공"),
        @ApiResponse(responseCode = "500", description = "메뉴 삭제 실패")
    })
    @DeleteMapping("/store/{storeId}/menu/{menuId}")
    public ResponseEntity<? extends BaseResponseBody> deleteMenu(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable UUID storeId,
        @PathVariable UUID menuId) {

        menuService.deleteMenu(userDetails.getId(), storeId, menuId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(0, null));
    }

}
