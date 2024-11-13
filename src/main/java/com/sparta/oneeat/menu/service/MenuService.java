package com.sparta.oneeat.menu.service;

import com.sparta.oneeat.common.exception.CustomException;
import com.sparta.oneeat.common.exception.ExceptionType;
import com.sparta.oneeat.menu.dto.request.AiRequestDto;
import com.sparta.oneeat.menu.dto.request.MenuRequestDto;
import com.sparta.oneeat.menu.dto.response.MenuResponseDto;
import com.sparta.oneeat.menu.entity.Menu;
import com.sparta.oneeat.menu.entity.MenuStatusEnum;
import com.sparta.oneeat.menu.repository.MenuRepository;
import com.sparta.oneeat.store.entity.Store;
import com.sparta.oneeat.store.repository.StoreRepository;
import com.sparta.oneeat.user.entity.User;
import com.sparta.oneeat.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final AiService aiService;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public MenuResponseDto createMenu(MenuRequestDto menuRequestDto, Long userId, UUID storeId) {
        // 검증
        if (menuRequestDto.getAiRequestDto().getMenuId() != null
            || menuRequestDto.getAiRequestDto().getUserId() != null) {
            throw new CustomException(ExceptionType.MENU_INVALID_REQUEST);
        }

        User user = userRepository.findById(userId)
            .orElseThrow(
                () -> new CustomException(ExceptionType.INTERNAL_SERVER_ERROR)); // 유저 없음
        Store store = storeRepository.findByIdAndUser(storeId, user)
            .orElseThrow(
                () -> new CustomException(ExceptionType.INTERNAL_SERVER_ERROR)); // 가게 없음

        // 메뉴 생성
        Menu menu = Menu.builder()
            .store(store)
            .name(menuRequestDto.getName())
            .description(menuRequestDto.getDescription())
            .price(menuRequestDto.getPrice())
            .image(menuRequestDto.getImage())
            .ai(menuRequestDto.getAi())
            .status(MenuStatusEnum.ON_SALE)
            .build();

        UUID menuId = menuRepository.save(menu).getId();

        // ai 플래그를 확인하여 true 라면 생성된 메뉴 Id와 함께 ai 질문, 응답을 저장
        if (menuRequestDto.getAi()) {
            AiRequestDto aiRequestDto = menuRequestDto.getAiRequestDto();
            aiRequestDto.setMenuId(menuId);
            aiRequestDto.setUserId(userId);
            aiService.saveAi(aiRequestDto);
        }

        return (new MenuResponseDto(menuRepository.findById(menuId)
            .orElseThrow(() -> new CustomException(ExceptionType.MENU_NOT_FOUND))));
    }
}

