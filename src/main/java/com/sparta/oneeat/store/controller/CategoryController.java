package com.sparta.oneeat.store.controller;

import com.sparta.oneeat.auth.service.UserDetailsImpl;
import com.sparta.oneeat.common.response.BaseResponseBody;
import com.sparta.oneeat.store.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Category", description = "Category API")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "가게 카테고리 삭제", description = "가게 카테고리를 삭제 처리합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "카테고리 삭제 성공"),
        @ApiResponse(responseCode = "500", description = "카테고리 삭제 실패")
    })
    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<? extends BaseResponseBody> hideCategory(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable UUID categoryId) {

        categoryService.deleteCategory(userDetails.getUser(), categoryId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(0, null));
    }

}
