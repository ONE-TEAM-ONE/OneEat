package com.sparta.oneeat.category.controller;

import com.sparta.oneeat.auth.service.UserDetailsImpl;
import com.sparta.oneeat.common.response.BaseResponseBody;
import com.sparta.oneeat.category.dto.CreateCategoryReqDto;
import com.sparta.oneeat.category.service.StoreCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
