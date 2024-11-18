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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final AiService aiService;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public MenuResponseDto createMenu(User user, MenuRequestDto menuRequestDto, UUID storeId) {

        Store store = validateStore(user, storeId);

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
            aiService.saveAi(aiRequestDto, user.getId(), menuId);
        }

        return (new MenuResponseDto(validateMenu(menuId)));
    }

    // 메뉴 상세 조회
    public MenuResponseDto getMenuDetail(User user, UUID storeId, UUID menuId) {
        // 유저 검증
        validateUser(user.getId());
        // 가게 검증
        Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new CustomException(ExceptionType.INTERNAL_SERVER_ERROR)); // 가게 없음

        // 해당 가게 상세 메뉴 반환
        Menu menu = menuRepository.findByIdAndStoreIdAndDeletedAtIsNull(menuId, store)
            .orElseThrow(() -> new CustomException(ExceptionType.MENU_INVALID_REQUEST));

        return new MenuResponseDto(menu);
    }

    // 가게의 모든 메뉴를 조회 (가격 내림)
    public Page<MenuResponseDto> getMenuList(UUID storeId, int page, int size,
        String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, sort));

        // 가게 검증
        Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new CustomException(ExceptionType.INTERNAL_SERVER_ERROR)); // 가게 없음

        // 정렬 조건에 맞게 해당 가게의 메뉴 전체를 가져온다
        Page<Menu> menus = menuRepository.findAllByStoreAndDeletedAtIsNull(store, pageable);

        return menus.map(MenuResponseDto::new);
    }

    @Transactional
    public MenuResponseDto updateMenu(User user, MenuRequestDto updateRequestDto,
        UUID storeId, UUID menuId) {

        log.info("userId {}", user.getId());

        Store store;

        if (user.getRole() == UserRoleEnum.OWNER) {
            store = validateStore(user, storeId);
        } else {
            throw new CustomException(ExceptionType.MENU_ACCESS_DENIED); // 권한 X
        }

        // 기존 메뉴는 숨김처리한다
        Menu menu = validateMenu(menuId);

        log.info("mene {}", menu.toString());

        menu.delete(user.getId());

        // 새로 추가된 메뉴를 반환한다
        return createMenu(user, updateRequestDto, storeId);
    }

    @Transactional
    public void updateMenuStatus(User user, UUID storeId, UUID menuId) {
        Store store;

        if (user.getRole() == UserRoleEnum.OWNER) {
            store = validateStore(user, storeId);
        } else {
            throw new CustomException(ExceptionType.MENU_ACCESS_DENIED); // 권한 X
        }

        // 상태 변경
        Menu menu = validateMenu(menuId);

        if (menu.getStatus() == MenuStatusEnum.ON_SALE) {
            menu.updateStatus(MenuStatusEnum.SOLD_OUT);
        } else {
            menu.updateStatus(MenuStatusEnum.ON_SALE);
        }
        menuRepository.save(menu);
    }

    @Transactional
    public void hideMenu(long userId, UUID storeId, UUID menuId) {
        // 유저 검증
        User user = validateUser(userId);

        Store store;

        if (user.getRole() == UserRoleEnum.OWNER) {
            store = validateStore(user, storeId);
        } else {
            throw new CustomException(ExceptionType.MENU_ACCESS_DENIED); // 권한 X
        }

        Menu menu = validateMenu(menuId);
        menu.delete(user.getId());
    }

    @Transactional
    public void deleteMenu(long userId, UUID storeId, UUID menuId) {
        // 유저 검증
        User user = validateUser(userId);

        if (!(user.getRole() == UserRoleEnum.MASTER || user.getRole() == UserRoleEnum.MANAGER)) {
            throw new CustomException(ExceptionType.MENU_ACCESS_DENIED); // 권한 없음
        }

        Store store = validateStore(user, storeId);

        Menu menu = menuRepository.findByIdAndStore(menuId, store)
            .orElseThrow(() -> new CustomException(ExceptionType.MENU_INVALID_REQUEST));

        menuRepository.delete(menu);
    }

    private User validateUser(long userId) {
        // 유저 검증
        return userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));
    }

    private Store validateStore(User user, UUID storeId) {
        // 가게 검증 / 사장이라면 해당 유저에게 해당 가게가 있는지
        if (user.getRole() == UserRoleEnum.OWNER) {
            return storeRepository.findByIdAndUser(storeId, user)
                .orElseThrow(
                    () -> new CustomException(
                        ExceptionType.INTERNAL_SERVER_ERROR)); //TODO 가게 ExceptionType으로 수정 필요
        } else {
            return storeRepository.findById(storeId).orElseThrow(
                () -> new CustomException(ExceptionType.INTERNAL_SERVER_ERROR)); //TODO 가게 ExceptionType으로 수정 필요
        }
    }

    private Menu validateMenu(UUID menuId) {
        return  menuRepository.findById(menuId)
            .orElseThrow(() -> new CustomException(ExceptionType.MENU_INVALID_REQUEST));
    }
}

