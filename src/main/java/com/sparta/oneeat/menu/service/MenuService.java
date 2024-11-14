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
import com.sparta.oneeat.user.entity.UserRoleEnum;
import com.sparta.oneeat.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
                () -> new CustomException(ExceptionType.INTERNAL_SERVER_ERROR)); // 가게 없음 (유저의 가게X)

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

    // 가게의 모든 메뉴를 조회 (가격 오름차순)
    public Page<MenuResponseDto> getMenuList(long userId, UUID storeId, int page, int size, String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, sort));

        // 유저 검증
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ExceptionType.INTERNAL_SERVER_ERROR));

        Store store;

        // 가게 검증 / 사장이라면 해당 유저에게 해당 가게가 있는지
        if (user.getRole() == UserRoleEnum.OWNER) {
            store = storeRepository.findByIdAndUser(storeId, user)
                .orElseThrow(
                    () -> new CustomException(
                        ExceptionType.INTERNAL_SERVER_ERROR)); // 가게 없음 (and 유저의 가게X)
        } else {
            store = storeRepository.findById(storeId).orElseThrow(
                () -> new CustomException(ExceptionType.INTERNAL_SERVER_ERROR)); // 가게 없음
        }

        // 정렬 조건에 맞게 해당 가게의 메뉴 전체를 가져온다
        Page<Menu> menus = menuRepository.findAllByStore(store, pageable);

        return menus.map(MenuResponseDto::new);
    }
}

