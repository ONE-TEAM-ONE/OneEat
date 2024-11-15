package com.sparta.oneeat.category.controller;

import com.sparta.oneeat.auth.service.UserDetailsImpl;
import com.sparta.oneeat.category.dto.CreateCategoryReqDto;
import com.sparta.oneeat.category.dto.UpdateCategoryReqDto;
import com.sparta.oneeat.category.service.StoreCategoryService;
import com.sparta.oneeat.common.response.BaseResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@Tag(name="Category", description="Category API")
public class StoreCategoryController {

    private final StoreCategoryService storeCategoryService;

    @Operation(summary = "가게 카테고리 생성", description = "가게 카테고리를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "카테고리 생성 성공"),
            @ApiResponse(responseCode = "500", description = "카테고리 생성 실패")
    })
    @PostMapping("")
    public ResponseEntity<? extends BaseResponseBody> createCategory(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Validated @RequestBody CreateCategoryReqDto createCategoryReqDto
    ){

        log.info("userId : {}", userDetails.getId());
        log.info("category : {}", createCategoryReqDto.getCategory());

        return ResponseEntity.status(201).body(BaseResponseBody.of(0, storeCategoryService.createCategory(userDetails.getId(), createCategoryReqDto)));
    }
  
    @Operation(summary = "가게 카테고리 수정", description = "가게 카테고리를 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "카테고리 수정 성공"),
        @ApiResponse(responseCode = "500", description = "카테고리 수정 실패")
    })
    @PutMapping("/{categoryId}")
    public ResponseEntity<? extends BaseResponseBody> updateCategory(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody UpdateCategoryReqDto requestDto,
        @PathVariable UUID categoryId) {

        storeCategoryService.updateCategory(userDetails.getUser(), requestDto, categoryId);

        return ResponseEntity.status(200).body(BaseResponseBody.of(0, null));
    }
  
    @Operation(summary = "가게 카테고리 삭제", description = "가게 카테고리를 삭제 처리합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "카테고리 삭제 성공"),
        @ApiResponse(responseCode = "500", description = "카테고리 삭제 실패")
    })
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<? extends BaseResponseBody> deleteCategory(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable UUID categoryId) {
        storeCategoryService.deleteCategory(userDetails.getUser(), categoryId);
      
        return ResponseEntity.status(200).body(BaseResponseBody.of(0, null));
    }
  
    @Operation(summary = "가게 카테고리 숨김", description = "가게 카테고리를 숨김 처리합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "카테고리 숨김 성공"),
        @ApiResponse(responseCode = "500", description = "카테고리 숨김 실패")
    })
    @PatchMapping("/{categoryId}")
    public ResponseEntity<? extends BaseResponseBody> hideCategory(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable UUID categoryId) {

        storeCategoryService.hideCategory(userDetails.getUser(), categoryId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(0, null));
    }

    @Operation(summary = "가게 카테고리 목록 조회", description = "가게 카테고리 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 카테고리 목록 조회 성공"),
            @ApiResponse(responseCode = "500", description = "가게 카테고리 목록 조회 실패")
    })
    @GetMapping("")
    public ResponseEntity<? extends BaseResponseBody> getStoreCategoryList(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "categoryName") String sort,
            @RequestParam(value = "isAsc", defaultValue = "true") boolean isAsc
    ){

        log.info("userId : {}", userDetails.getId());
        log.info("page : {}", page);
        log.info("size : {}", size);
        log.info("sort : {}", sort);
        log.info("isAsc : {}", isAsc);

        return ResponseEntity.status(200).body(BaseResponseBody.of(0, storeCategoryService.getStoreCategoryList(userDetails.getId(), (page-1), size, sort, isAsc)));
    }
  
}
